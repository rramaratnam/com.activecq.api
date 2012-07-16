/*
 * Copyright 2012 david.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.activecq.api;

import com.activecq.api.plugins.*;
import com.activecq.api.utils.TextUtil;
import com.day.cq.commons.inherit.HierarchyNodeInheritanceValueMap;
import com.day.cq.search.QueryBuilder;
import com.day.cq.wcm.api.Page;
import com.day.cq.wcm.api.PageManager;
import com.day.cq.wcm.api.components.Component;
import com.day.cq.wcm.api.components.ComponentContext;
import com.day.cq.wcm.api.components.EditContext;
import com.day.cq.wcm.api.designer.Design;
import com.day.cq.wcm.api.designer.Designer;
import com.day.cq.wcm.api.designer.Style;
import com.day.cq.wcm.commons.WCMUtils;
import javax.jcr.Node;
import javax.jcr.RepositoryException;
import javax.servlet.jsp.PageContext;
import org.apache.commons.lang.StringUtils;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.resource.*;
import org.apache.sling.api.scripting.SlingBindings;
import org.apache.sling.api.scripting.SlingScriptHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class ActiveComponent implements ActivePresenter {

    @SuppressWarnings("unused")
    private static final Logger log = LoggerFactory.getLogger(ActiveComponent.class);
        
    /*
     * Universal Fields
     */
    private SlingScriptHelper slingScriptHelper;
    private SlingHttpServletRequest request;
    private SlingHttpServletResponse response;
    private Component component;
    private Node node;
    private Resource resource;
    private ComponentContext componentContext;
    private EditContext editContext;
    
    /*
     * Request Fields
     */
    private Page requestPage;
    private Design requestDesign;
    private Style requestStyle;
    private ValueMap requestPageProperties;
    private ValueMap requestDesignProperties;

    /*
     * Resource Fields
     */
    private ValueMap resourceProperties;
    private Page resourcePage;
    private Design resourceDesign;
    private Style resourceStyle;
    private ValueMap resourcePageProperties;
    private ValueMap resourceDesignProperties;
    
    /*
     * Utility Fields
     */
    private Designer designer;
    private ResourceResolver resourceResolver;
    private QueryBuilder queryBuilder;
    private PageManager pageManager;

    /**
     * Plug-ins
     */
    public class PluginsWrapper {
        public WCMModePlugin WCMMode;
        public XSSPlugin XSS;
        public I18nPlugin I18n;
        public DiffPlugin Diff;
        public PersistencePlugin Persistence;
    };

    public enum Options {
        NO_DIFF, RAW, NO_XSS, NO_I18N;

        public boolean isIn(Options... options) {
            if (options == null) {
                return false;
            }
            for (Options option : options) {
                if (this.equals(option)) {
                    return true;
                }
            }

            return false;
        }
    };
    
    protected PluginsWrapper Plugins = new PluginsWrapper();

    /**
     * Constructor used in conjunction with JSTL/EL
     *
     * @param slingScriptHelper
     * @param skipPlugins
     * @throws RepositoryException
     * @throws LoginException
     */
    protected ActiveComponent(SlingHttpServletRequest request)
            throws RepositoryException, LoginException {
        
        // HTTP Request
        this.request = request;
        if (this.request == null) {
            throw new IllegalArgumentException(
                    "Sling HTTP Request must NOT be null.");
        }
        
        // Get SlingScriptHelper from SlingBindings included as an attribute on the Sling HTTP Request 
        SlingBindings slingBindings = (SlingBindings) this.request.getAttribute(SlingBindings.class.getName());
        if (slingBindings == null) {
            throw new IllegalArgumentException(
                    "SlingHttpServletRequest most have contain a SlingBindings object at key: " + SlingBindings.class.getName());
        }

        // Sling Script Helper
        this.slingScriptHelper = slingBindings.getSling();
        if (this.slingScriptHelper == null) {
            throw new IllegalArgumentException(
                    "SlingScriptHelper must NOT be null.");
        }

        // HTTP Response
        this.response = this.slingScriptHelper.getResponse();
        if (this.response == null) {
            throw new IllegalArgumentException(
                    "SlingScriptHelper's Sling HTTP Response must NOT be null.");
        }

        // Resource of component (Regardless of which page/resource it lives under)
        this.resource = this.request.getResource();
        if (this.resource == null) {
            throw new IllegalArgumentException("Resource must NOT be null.");
        }

        // CQ Component object
        this.component = WCMUtils.getComponent(this.resource);
        if (this.component == null) {
            throw new IllegalArgumentException(
                    "Resource must have a resourceType of a CQ Component.");
        }

        // JCR Node
        this.node = WCMUtils.getNode(this.resource);

        // Initialize Plugins
        /*
        * General Helpers
        */
        this.Plugins.Persistence = new PersistencePlugin(this.request);
        this.Plugins.WCMMode = new WCMModePlugin(this.request);
        /*
        * Text manipulation
        */
        this.Plugins.XSS = new XSSPlugin(this.request);
        this.Plugins.I18n = new I18nPlugin(this.request);
        this.Plugins.Diff = new DiffPlugin(this.request);              
    }
    
    
    /**
     * *************************************************************************
     * Request & Response
     *************************************************************************
     */
    /**
     * Returns the Sling Request that initiated the request for this component
     *
     * @return
     */
    protected SlingHttpServletRequest getRequest() {
        return this.request;
    }

    /**
     * Returns the Sling Response that will be used to transport this component
     * rendition to the client
     *
     * @return
     */
    protected SlingHttpServletResponse getResponse() {
        return this.response;
    }

    /**
     * *************************************************************************
     * Getters for Component Resource
     *
     * These will deal directly with the Resource node instance that represents
     * a particular instance of a Component and does care which Page the
     * component actually resides on.
     *************************************************************************
     */
    /**
     * Getter for Resource object that represents the Component Instance
     *
     * http://dev.day.com/docs/en/cq/current/javadoc/org/apache/sling/api/resource/Resource.html
     *
     * @return Resource representing the component
     */
    protected Resource getResource() {
        return this.resource;
    }

    /**
     * Getter for the Component Class
     *
     * This object represents the Component implementation under /apps or /libs)
     *
     * http://dev.day.com/docs/en/cq/current/javadoc/com/day/cq/wcm/api/components/ComponentContext.html
     *
     * @return Component representing the component
     */
    protected Component getComponent() {
        return this.component;
    }

    /**
     * Check this component has a valid node
     *
     * @return
     */
    protected boolean hasNode() {
        return this.node != null;
    }

    /**
     * Getter for Resource's JCR Node. JCR Node should be used only for modify
     * the Node. Sling APIs should be used to read from the resources.
     *
     * http://www.day.com/maven/jsr170/javadocs/jcr-1.0/javax/jcr/Node.html
     *
     * @return JCR Node representing the component
     */
    protected Node getNode() {
        return this.node;
    }

    /**
     * Get for the Component's ComponentContext for the Request
     *
     * The Component Context's context is that of the Request, not necessarily
     * the page that contains the component.
     *
     * http://dev.day.com/docs/en/cq/current/javadoc/com/day/cq/wcm/api/components/ComponentContext.html
     *
     * @return
     */
    protected ComponentContext getComponentContext() {
        if (this.componentContext == null) {
            this.componentContext = WCMUtils.getComponentContext(this.getRequest());
        }

        return this.componentContext;
    }

    /**
     * Get the Component's Edit Context
     *
     * http://dev.day.com/docs/en/cq/current/javadoc/com/day/cq/wcm/api/components/EditContext.html
     *
     * @return
     */
    protected EditContext getEditContext() {
        if (this.editContext == null) {
            if (this.getComponentContext() != null) {
                this.editContext = this.getComponentContext().getEditContext();
            }
        }

        return this.editContext;
    }

    /**
     * *************************************************************************
     * Lazy-loading Getters for Request-oriented Attributes
     *************************************************************************
     */
    /**
     * Getter for the Request Resource's Page object.
     *
     * This represents the page resource that was requested, and sling includes
     * the component.
     *
     * http://dev.day.com/docs/en/cq/current/javadoc/com/day/cq/wcm/api/Page.html
     *
     * @return
     */
    protected Page getRequestPage() {
        if (this.requestPage == null) {
            if (this.getComponentContext() != null) {
                this.requestPage = this.getComponentContext().getPage();

                if (this.requestPage == null) {
                    this.requestPage = this.getResourcePage();
                }
            }
        }

        return this.requestPage;
    }

    /**
     * Convenience method.
     *
     * Handles the "normal" case of using the Request Page's Page object.
     *
     * @return
     */
    protected Page getPage() {
        return this.getRequestPage();
    }

    /**
     * Getter for the Requested Page's Design
     *
     * http://dev.day.com/docs/en/cq/current/javadoc/index.html?com/day/cq/wcm/api/designer/class-use/Design.html
     *
     * @return
     */
    protected Design getRequestDesign() {
        if (this.requestDesign == null) {
            if (this.getDesigner() != null) {
                if (this.getRequestPage() != null) {
                    this.requestDesign = this.getDesigner().getDesign(this.getRequestPage());
                }
            }
        }

        return this.requestDesign;
    }

    /**
     * Convenience method.
     *
     * Handles the "normal" case of using the Request Page's Design
     *
     * @return
     */
    protected Design getDesign() {
        return this.getRequestDesign();
    }

    /**
     * Gets the Page Properties for the Page associated with Request
     *
     * @return
     */
    protected ValueMap getRequestPageProperties() {
        if (this.requestPageProperties == null) {
            if (this.getRequestPage() != null) {
                this.requestPageProperties = new HierarchyNodeInheritanceValueMap(this.getRequestPage().getContentResource());
            }
        }

        return this.requestPageProperties;
    }

    /**
     * Alias for getPageProperties()
     *
     * @return
     */
    protected ValueMap getPageProperties() {
        return this.getRequestPageProperties();
    }

    /**
     * Gets the Page Properties for the Page associated with the Resource
     *
     * @return
     */
    protected ValueMap getResourcePageProperties() {
        if (this.resourcePageProperties == null) {
            if (this.getResourcePage() != null) {
                this.resourcePageProperties = new HierarchyNodeInheritanceValueMap(this.getResourcePage().getContentResource());
            }
        }

        return this.resourcePageProperties;
    }

    /**
     * Getter for the Component instance's Style
     *
     * @return
     */
    protected Style getRequestStyle() {
        if (this.requestStyle == null) {
            if (this.getRequestDesign() != null && this.getComponentContext() != null) {
                this.requestStyle = this.getRequestDesign().getStyle(this.getComponentContext().getCell());
            }
        }

        return this.requestStyle;
    }

    /**
     * Alias for getRequestStyle()
     *
     * @return
     */
    protected Style getStyle() {
        return this.getRequestStyle();
    }

    /**
     * Get a ValueMap of the properties for the content Resource's Design
     *
     * @return
     */
    protected ValueMap getRequestDesignProperties() {
        if (this.requestDesignProperties == null) {
            this.requestDesignProperties = this.getRequestStyle();
        }

        return this.requestDesignProperties;
    }

    protected ValueMap getDesignProperties() {
        return this.getRequestDesignProperties();
    }

    /**
     * *************************************************************************
     * Lazy-loading Getters for Resource-oriented Data
     *************************************************************************
     */
    /**
     * Getter for the Component instance's Page object.
     *
     * This represents the page resource who is the parent of the component
     * instance. This may not be the same as the getRequetsPage() Page.
     *
     * http://dev.day.com/docs/en/cq/current/javadoc/com/day/cq/wcm/api/Page.html
     *
     * @return
     */
    protected Page getResourcePage() {
        if (this.resourcePage == null) {
            if (this.getPageManager() != null) {
                this.resourcePage = this.getPageManager().getContainingPage(
                        this.getResource());
            }
        }

        return this.resourcePage;
    }

    /**
     * Getter for the Component Page's Design
     *
     * http://dev.day.com/docs/en/cq/current/javadoc/index.html?com/day/cq/wcm/api/designer/class-use/Design.html
     *
     * @return
     */
    protected Design getResourceDesign() {
        if (this.resourceDesign == null) {
            if (this.getDesigner() != null) {
                if (this.getRequestPage() != null) {
                    this.resourceDesign = this.getDesigner().getDesign(this.getResourcePage());
                }
            }
        }

        return this.resourceDesign;
    }

    /** 
     * Get the style associated with the Resource's Page
     * 
     * @return 
     */
    protected Style getResourceStyle() {
        if (this.resourceStyle == null) {
            if (this.getResourceDesign() != null && this.getComponentContext() != null) {
                this.resourceStyle = this.getResourceDesign().getStyle(this.getComponentContext().getCell());
            }
        }

        return this.resourceStyle;
    }

    /**
     * Get a ValueMap of properties for the content Resource
     *
     * @return
     */
    protected ValueMap getProperties() {
        if (this.resourceProperties == null) {
            this.resourceProperties = ResourceUtil.getValueMap(this.getResource());
        }

        return this.resourceProperties;
    }

    /**
     * Get the Design properties associated with the Resource's style (rather
     * than the Requests')
     *
     * @return
     */
    protected ValueMap getResourceDesignProperties() {
        if (this.resourceDesignProperties == null) {
            this.resourceDesignProperties = this.getResourceStyle();
        }

        return this.resourceDesignProperties;
    }

    /**
     * *************************************************************************
     * Lazy-loading Getters for Universal Helper and Utility objects
     *************************************************************************
     */
    /**
     * Getter for a CQ Designer for the Sling Request's user
     *
     * @return
     */
    protected Designer getDesigner() {
        if (this.designer == null) {
            this.designer = this.getResourceResolver().adaptTo(Designer.class);
        }

        return this.designer;
    }

    /**
     * Getter for a CQ PageManager for the Sling Request's user
     *
     * @return
     */
    protected PageManager getPageManager() {
        if (this.pageManager == null) {
            if (this.getResourceResolver() != null) {
                this.pageManager = this.getResourceResolver().adaptTo(
                        PageManager.class);
            }
        }

        return this.pageManager;
    }

    /**
     * Getter for a CQ Resource Resolver for the Sling Request's user
     *
     * @return
     */
    protected ResourceResolver getResourceResolver() {
        if (this.resourceResolver == null) {
            this.resourceResolver = this.getRequest().getResourceResolver();
        }

        return this.resourceResolver;
    }

    /**
     * Getter for a CQ Query Builder for the Sling Request's user
     *
     * @return
     */
    protected QueryBuilder getQueryBuilder() {
        if (this.queryBuilder == null) {
            this.queryBuilder = this.getResourceResolver().adaptTo(
                    QueryBuilder.class);
        }

        return this.queryBuilder;
    }

    /**
     * *************************************************************************
     * OSGi Service Exposure Methods
     *************************************************************************
     */
    /**
     * Exposes ability to get services from within the ActiveComponent class,
     * which itself is not a Sling Service
     *
     * @param <ServiceType>
     * @param type
     * @return the requested service or null.
     */
    protected <ServiceType> ServiceType getService(Class<ServiceType> type) {
        if (this.slingScriptHelper != null) {
            return slingScriptHelper.getService(type);
        } else {
            throw new IllegalStateException(
                    "SlingScriptHelper is NULL");
        }
    }

    /**
     * *************************************************************************
     * Property Getter Methods
     *************************************************************************
     */
    // Resource (Dialog) Properties
    public <T> T getProperty(String key, Class<T> klass) {
        return getProperty(key, klass, new Options[]{});
    }

    public <T> T getProperty(String key, Class<T> klass, Options... options) {
        if (String.class.equals(klass)) {
            final String current = (String) getPropertyGeneric(this.getResource(), key, klass, options);
            return (T) this.getDiffText(this.getResource(), key, current, options);
        } else {
            return getPropertyGeneric(this.getResource(), key, klass, options);
        }
    }

    public <T> T getProperty(String key, T defaultValue) {
        return getProperty(key, defaultValue, new Options[]{});
    }

    public <T> T getProperty(String key, T defaultValue, Options... options) {
        if (defaultValue instanceof String) {
            final String current = (String) getPropertyGeneric(this.getResource(), key, defaultValue, options);
            return (T) this.getDiffText(this.getResource(), key, current, options);
        } else {
            return getPropertyGeneric(this.getResource(), key, defaultValue, options);
        }
    }

    // Design Dialog Properties
    public <T> T getDesignProperty(String key, Class<T> klass) {
        return getDesignProperty(key, klass, new Options[]{});
    }

    public <T> T getDesignProperty(String key, Class<T> klass, Options... options) {
        return getPropertyGeneric(this.getRequestDesignProperties(), key, klass, options);
    }

    public <T> T getDesignProperty(String key, T defaultValue) {
        return getDesignProperty(key, defaultValue, new Options[]{});
    }

    public <T> T getDesignProperty(String key, T defaultValue, Options... options) {
        return getPropertyGeneric(this.getRequestDesignProperties(), key, defaultValue, options);
    }

    // Any Resource
    public <T> T getProperty(Resource resource, String key, Class<T> klass) {
        return getProperty(resource, key, klass, new Options[]{});
    }

    public <T> T getProperty(Resource resource, String key, Class<T> klass, Options... options) {
        if (resource == null) {
            return null;
        }
        return getPropertyGeneric(resource, key, klass, options);
    }

    public <T> T getProperty(Resource resource, String key, T defaultValue) {
        return getProperty(resource, key, defaultValue, new Options[]{});
    }

    public <T> T getProperty(Resource resource, String key, T defaultValue, Options... options) {
        if (resource == null) {
            return null;
        }
        return getPropertyGeneric(resource, key, defaultValue, options);
    }

    // Expose Diff Text functionality of Diff Plugin    
    public String getDiffText(Resource resource, String key, String current, Options... options) {
        if (this.Plugins.Diff.isEnabled()) {

            if (Options.RAW.isIn(options) || Options.NO_DIFF.isIn(options)) {
                return current;
            }

            if (current == null) {
                current = this.Plugins.Diff.getCurrentText(resource, key, current);
                current = this.process(current, options);
            }

            String previous = this.Plugins.Diff.getPreviousText(resource, key);
            previous = this.process(previous, key, options);

            return this.Plugins.Diff.getDiff(resource, current, previous,
                    (TextUtil.isRichText(current) || TextUtil.isRichText(previous)));
        } else {
            return current;
        }
    }

    private <T> T getPropertyGeneric(Resource resource, String key, Class<T> klass, Options... options) {
        if (resource == null) {
            return null;
        }

        final ValueMap valueMap = resource.adaptTo(ValueMap.class);
        return this.getPropertyGeneric(valueMap, key, klass, options);
    }

    @SuppressWarnings("unchecked")
    private <T> T getPropertyGeneric(ValueMap valueMap, String key, Class<T> klass, Options... options) {
        if (valueMap == null || klass == null) {
            return null;
        }

        if (!valueMap.containsKey(key)) {
            return null;
        }

        boolean isString = String.class.equals(klass);

        if (isString) {
            // Strip leading and trailing whitespace
            String strValue = StringUtils.strip((String) valueMap.get(key, klass));
            strValue = this.process(strValue, key, options);

            return (T) strValue;
        } else {
            return valueMap.get(key, klass);
        }
    }

    private <T> T getPropertyGeneric(Resource resource, String key, T defaultValue, Options... options) {
        if (resource == null) {
            return null;
        }

        final ValueMap valueMap = resource.adaptTo(ValueMap.class);
        return this.getPropertyGeneric(valueMap, key, defaultValue, options);
    }

    @SuppressWarnings("unchecked")
    private <T> T getPropertyGeneric(ValueMap valueMap, String key, T defaultValue, Options... options) {
        if (valueMap == null) {
            return defaultValue;
        }

        if (!valueMap.containsKey(key)) {
            return defaultValue;
        }

        boolean isString = false;
        if (defaultValue != null) {
            isString = String.class.equals(defaultValue.getClass());
        }

        if (isString) {
            // Strip leading and trailing whitespace
            String strValue = StringUtils.strip((String) valueMap.get(key, defaultValue));
            strValue = this.process(strValue, key, options);
            return (T) strValue;
        } else {
            return valueMap.get(key, defaultValue);
        }
    }

    /**
     * Process text using the XSS and i18n Plugins
     *
     * This method only looks to the Options param to determine which filters
     * should be run.
     *
     * @param strValue
     * @param options
     * @return
     */
    public String process(String strValue, Options... options) {
        if (Options.RAW.isIn(options)) {
            return strValue;
        }

        if (!Options.NO_XSS.isIn(options)) {
            strValue = this.Plugins.XSS.protect(strValue);
        }

        if (!Options.NO_I18N.isIn(options)) {
            strValue = this.Plugins.I18n.translate(strValue);
        }

        return strValue;
    }

    /**
     * Process text using the XSS and i18n Plugins
     *
     * This method only looks to the Options param and to any Plugin setup (key
     * based) to determine which filters should be run.
     *
     * @param strValue
     * @param key
     * @param options
     * @return
     */
    public String process(String strValue, String key, Options... options) {
        if (Options.RAW.isIn(options)) {
            return strValue;
        }

        if (!Options.NO_XSS.isIn(options)) {
            strValue = this.Plugins.XSS.protect(strValue, key);
        }

        if (!Options.NO_I18N.isIn(options)) {
            strValue = this.Plugins.I18n.translate(strValue);
        }

        return strValue;
    }
    
    /**
     * 
     * @return 
     */
    public abstract ActiveModel getModel();
}
