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
package com.activecq.api.test;

import com.activecq.api.ActiveComponent;
import com.day.cq.search.QueryBuilder;
import com.day.cq.wcm.api.Page;
import com.day.cq.wcm.api.PageManager;
import com.day.cq.wcm.api.components.Component;
import com.day.cq.wcm.api.components.ComponentContext;
import com.day.cq.wcm.api.components.EditContext;
import com.day.cq.wcm.api.designer.Design;
import com.day.cq.wcm.api.designer.Designer;
import com.day.cq.wcm.api.designer.Style;
import javax.jcr.Node;
import javax.jcr.RepositoryException;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.resource.LoginException;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.resource.ValueMap;

/**
 *
 * @author david
 */
public class ActiveComponentTestStub extends ActiveComponent {
    public ActiveComponentTestStub(SlingHttpServletRequest request) throws RepositoryException, LoginException {
        super(request);
    }    
    
    @Override
    public SlingHttpServletRequest getRequest() {
        return super.getRequest();
    }

    @Override
    public SlingHttpServletResponse getResponse() {
        return super.getResponse();
    }

    @Override
    public Resource getResource() {
        return super.getResource();
    }

    @Override
    public Component getComponent() {
        return super.getComponent();
    }

    @Override
    public boolean hasNode() {
        return super.hasNode();
    }

    @Override
    public Node getNode() {
        return super.getNode();
    }

    @Override
    public ComponentContext getComponentContext() {
        return super.getComponentContext();
    }

    @Override
    public EditContext getEditContext() {
        return super.getEditContext();
    }

    @Override
    public Style getStyle() {
        return super.getStyle();
    }

    @Override
    public Resource getDesignResource() {
        return super.getDesignResource();
    }    
    
    @Override
    public ValueMap getProperties() {
        return super.getProperties();
    }

    @Override
    public ValueMap getDesignProperties() {
        return super.getDesignProperties();
    }

    @Override
    public Page getRequestPage() {
        return super.getRequestPage();
    }

    @Override
    public Page getPage() {
        return super.getPage();
    }

    @Override
    public Design getRequestDesign() {
        return super.getRequestDesign();
    }

    @Override
    public Design getDesign() {
        return super.getDesign();
    }

    @Override
    public Page getResourcePage() {
        return super.getResourcePage();
    }

    @Override
    public Design getResourceDesign() {
        return super.getResourceDesign();
    }

    @Override
    public Designer getDesigner() {
        return super.getDesigner();
    }

    @Override
    public PageManager getPageManager() {
        return super.getPageManager();
    }

    @Override
    public ResourceResolver getResourceResolver() {
        return super.getResourceResolver();
    }

    @Override
    public QueryBuilder getQueryBuilder() {
        return super.getQueryBuilder();
    }
    
    @Override
    public <ServiceType> ServiceType getService(Class<ServiceType> type) {
        return super.getService(type);
    }
    
}
