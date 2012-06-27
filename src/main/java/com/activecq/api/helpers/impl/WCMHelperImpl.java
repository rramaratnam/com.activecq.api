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
package com.activecq.api.helpers.impl;

import com.activecq.api.ActiveComponent;
import com.activecq.api.helpers.WCMEditType;
import com.activecq.api.helpers.WCMHelper;
import com.activecq.api.plugins.CorePlugin;
import com.day.cq.wcm.api.WCMMode;
import com.day.cq.wcm.commons.WCMUtils;
import java.io.IOException;
import java.util.Set;
import org.apache.felix.scr.annotations.*;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.scripting.SlingScriptHelper;
import org.apache.sling.settings.SlingSettingsService;
import org.osgi.service.component.ComponentContext;

/**
 * WCMHelper provides convenience functionality for accessing the CQ WCM aspects
 * of the page focusing on different differing views of pages and shortcuts for
 * authoring functionality.
 */

@Component(label="ActiveCQ - Helpers - WCM",
        description="WCMHelper provides convenience functionality for accessing the CQ WCM aspects of the page focusing on different differing views of pages and shortcuts for authoring functionality.",
        immediate=true)

@Properties({ 
    @Property(label="Vendor",
            name="service.vendor",
            value="ActiveCQ",
            propertyPrivate=true)
})

@Service
public class WCMHelperImpl implements WCMHelper {

    @Reference
    private SlingSettingsService slingSettingsService;

    /**
     * Checks if Page is in WCM Mode DESIGN
     * 
     * @return if current request is in Edit mode.
     */
    public boolean isDesignMode(SlingHttpServletRequest request) {
        return WCMMode.DESIGN.equals(WCMMode.fromRequest(request));
    }

    /**
     * Checks if Page is in WCM Mode DISABLED
     * 
     * @return if current request is in Edit mode.
     */
    public boolean isDisabledMode(SlingHttpServletRequest request) {
        return WCMMode.DISABLED.equals(WCMMode.fromRequest(request));
    }

    /**
     * Checks if Page is in WCM Mode EDIT
     * 
     * @return
     */
    public boolean isEditMode(SlingHttpServletRequest request) {
        return WCMMode.EDIT.equals(WCMMode.fromRequest(request));
    }

    /**
     * Checks if Page is in WCM Mode PREVIEW
     * 
     * @return if current request is in Edit mode.
     */
    public boolean isPreviewMode(SlingHttpServletRequest request) {
        return WCMMode.PREVIEW.equals(WCMMode.fromRequest(request));
    }

    /**
     * Checks if Page is in WCM Mode READ_ONLY
     * 
     * @return if current request is in Edit mode.
     */
    public boolean isReadOnlyMode(SlingHttpServletRequest request) {
        return WCMMode.READ_ONLY.equals(WCMMode.fromRequest(request));
    }

    /**
     * Checks if the mode is in an "Authoring" mode; Edit or Design.
     * 
     * @param request
     * @return
     */
    public boolean isAuthoringMode(SlingHttpServletRequest request) {
        return (isEditMode(request) || isDesignMode(request));
    }

    /**
     * Checks for the Author Sling run mode
     * 
     * Use carefully, code should be the same between author and publish
     * 
     * @return
     */
    public boolean isAuthor() {
        Set<String> runModes = slingSettingsService.getRunModes();
        return runModes.contains("author");
    }

    /**
     * Checks for the Author Sling run mode
     * 
     * Use carefully, code should be the same between author and publish
     * 
     * @return
     */
    public boolean isPublish() {
        Set<String> runModes = slingSettingsService.getRunModes();
        return runModes.contains("publish");
    }

    /**
     * 
     */
    

    
    public boolean printEditBlock(SlingScriptHelper sling,
            WCMEditType editType,
            boolean... conditions) {

        final SlingHttpServletRequest request = sling.getRequest();
        final SlingHttpServletResponse response = sling.getResponse();
        final Resource resource = sling.getRequest().getResource();
        final com.day.cq.wcm.api.components.Component component = WCMUtils.getComponent(resource);
        final String title = component.getTitle();
            
        if (!isAuthoringMode(request)
                && conditionAndCheck(conditions)) {
            return false;
        }
        
        String html = "<div class=\"edit-mode\" style=\"min-width: 100px;\">";

        if(component == null) {
            html += "Could not resolve CQ Component type.";
        } else if (editType.equals(WCMEditType.NOICON) || editType.equals(WCMEditType.NONE)) {
            html += "<dl>";
            html += "<dt>" + title + "</dt>";

            if (component.getDialogPath() != null) {
                html += "<dd>Double click or Right click to Edit</dd>";
            }

            if (component.getDesignDialogPath() != null) {
                html += "<dd>Switch to Design mode and click the Edit button</dd>";
            }

            if (component.getDialogPath() == null
                    && component.getDesignDialogPath() == null) {
                html += "<dd>The component cannot be edited</dd>";
            }

            html += "</dl>";
        } else {
            html += "<img" + " " + "src=\"/libs/cq/ui/resources/0.gif\"" + " "
                    + "class=\"cq-" + editType.name().toLowerCase()
                    + "-placeholder\"" + " " + "alt=\"" + title + "\"" + " "
                    + "title=\"" + title + "\"" + "/>";
        }

        html += "</div>";

        try {
            response.getWriter().print(html);
            response.getWriter().flush();
        } catch (IOException e) {
            return false;
        }

        return true;
    }

    private boolean conditionAndCheck(boolean... conditions) {
        if (conditions == null) {
            return false;
        }

        for (int i = 0; i < conditions.length; i++) {
            if (!conditions[i]) {
                return false;
            }
        }

        return true;
    }

    protected void activate(ComponentContext componentContext) {
        // Dictionary properties = componentContext.getProperties();
    }

    protected void deactivate(ComponentContext componentContext) {
    }

}
