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

import com.day.cq.wcm.api.components.Component;
import java.util.HashMap;
import org.apache.sling.api.SlingHttpServletRequest;

/**
 *
 * @author david
 */
public class PersistencePlugin {

    private Component component;
    private SlingHttpServletRequest request;

    public PersistencePlugin(ExposedPlugin exposed) {
        this.component = exposed.getComponent();
        this.request = exposed.getRequest();
    }

    /**
     * Used to persist data between Components on the same HTTP request.
     *
     * @param key
     * @param value
     */
    @SuppressWarnings("unchecked")
    public String set(SlingHttpServletRequest request,
            String key, Object value) {
        HashMap<String, Object> hashMap = (HashMap<String, Object>) this.request.getAttribute(this.component.getPath());

        if (hashMap == null) {
            hashMap = new HashMap<String, Object>();
        }

        hashMap.put(key, value);
        request.setAttribute(this.component.getName(), hashMap);

        return key;
    }

    /**
     * Used to retrieve persisted data between Components on the same HTTP
     * request.
     *
     * @param key
     * @return
     */
    @SuppressWarnings("unchecked")
    public Object get(SlingHttpServletRequest request, String key) {
        HashMap<String, Object> hashMap = (HashMap<String, Object>) this.request.getAttribute(this.component.getPath());

        if (hashMap == null) {
            return null;
        }

        return hashMap.get(key);
    }
}
