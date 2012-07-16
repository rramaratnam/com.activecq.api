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

import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.scripting.SlingBindings;
import org.apache.sling.api.scripting.SlingScriptHelper;

/**
 *
 * @author david
 */
public class BasePlugin {
    protected final SlingHttpServletRequest request;
    protected final SlingScriptHelper slingScriptHelper;
    protected final Resource resource;

    public BasePlugin(SlingHttpServletRequest request) {
        this.request = request;
        
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
        
        // Resource
        this.resource = this.request.getResource();
        if (this.resource == null) {
            throw new IllegalArgumentException("Resource must NOT be null.");
        }        
    }

    /**
     * Exposes ability to get services from within this class and subclasses
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
}
