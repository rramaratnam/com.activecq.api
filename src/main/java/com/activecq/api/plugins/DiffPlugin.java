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
package com.activecq.api.plugins;

import com.activecq.api.utils.HttpRequestUtil;
import com.day.cq.commons.DiffInfo;
import com.day.cq.commons.DiffService;
import com.day.cq.wcm.api.WCMMode;
import org.apache.commons.lang.StringUtils;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ValueMap;

/**
 *
 * @author david
 */
public class DiffPlugin extends BasePlugin {
    private static final String REQUEST_PARAM = "cq_diffTo";
    private final DiffService diffService;
    
    private boolean enabled;
    
    public DiffPlugin(SlingHttpServletRequest request) {
        super(request);
        
        // Must be in Diff Mode and Preview Mode to be enabled
        this.enabled = HttpRequestUtil.hasParameter(this.request, REQUEST_PARAM) &&
                WCMMode.PREVIEW.equals(WCMMode.fromRequest(this.request));
        this.diffService = this.getService(DiffService.class);        
    }
    
    public boolean isEnabled() {
        return this.enabled;
    }

    public void enable() {
        this.enabled = true;
    }    

    public void disable() {
        this.enabled = false;
    }    
    
    public String getCurrentText(Resource resource, String key, String current) {
        if(!this.isEnabled()) {
            return current;                    
        } else if(resource == null || StringUtils.stripToNull(key) == null) {
            return current;
        }
        
        // Get "current"
        if(StringUtils.stripToNull(current) == null) {
            ValueMap valueMap = resource.adaptTo(ValueMap.class);
            current = valueMap.get(key, String.class);            
        } 
        
        return current;
    }    
    
    public String getPreviousText(Resource resource, String key) {
        if(!this.isEnabled()) { return null; }
        
        DiffInfo diffInfo = resource.adaptTo(DiffInfo.class);
        ValueMap diffValueMap = diffInfo.getContent().adaptTo(ValueMap.class);
        return diffValueMap.get(key, String.class);                
    }
    
    public String getDiff(Resource resource, String current, String previous, boolean isRichText) {
        DiffInfo diffInfo = resource.adaptTo(DiffInfo.class);        
        return diffInfo.getDiffOutput(this.diffService, current, previous, isRichText);
    }
}
