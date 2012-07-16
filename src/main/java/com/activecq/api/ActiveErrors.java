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

import com.activecq.api.utils.CookieUtil;
import com.activecq.api.utils.TypeUtil;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;
import org.apache.commons.lang.StringUtils;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.request.RequestParameter;
import org.apache.sling.commons.json.JSONException;
import org.apache.sling.commons.json.JSONObject;

public class ActiveErrors {

    public static final String CQ_ERRORS = "_cq_e";

    /**
     * Private data modeling attributes
     */
    private Map<String, String> data;

    public ActiveErrors() {
        this.data = new HashMap<String, String>();
    }
        
    /**
     * Constructor for modeling the data object represented in the parameter Map
     * 
     * @param map
     */
    public ActiveErrors(Map<String, String> map) {
        this.data = new HashMap<String, String>(map);
    }



    /**
     * Checks if an error exists
     * 
     * @param key
     * @return boolean denoting if an error exists for the parameter key
     */
    public boolean has(String key) {
        return this.data.containsKey(key);
    }

    /**
     * Checks if an error exists
     * 
     * @param key
     * @return boolean denoting if an error exists for the parameter key
     */
    public boolean hasAny(String... keys) {
        for (int i = 0; i < keys.length; i++) {
            if (this.data.containsKey(keys[i])) {
                return true;
            }
        }
        return false;
    }

    /**
     * Checks if an error exists
     * 
     * @param key
     * @return boolean denoting if an error exists for the parameter key
     */
    public boolean hasAll(String... keys) {
        for (int i = 0; i < keys.length; i++) {
            if (!this.data.containsKey(keys[i])) {
                return false;
            }
        }
        return true;
    }

    /**
     * Gets a String value from this.data
     * 
     * @param key
     * @return
     */
    public String get(String key) {
        if (this.has(key)) {
            return (String) this.data.get(key);
        }

        return null;
    }

    /**
     * Stores a key/value into this.data
     * 
     * @param key
     * @param value
     */
    public void set(String key, String value) {
        this.data.put(key, value);
    }

    /**
     * Stores a key into this.data
     * 
     * @param key
     */
    public void set(String key) {
        this.set(key, null);
    }

    /**
     * Takes all the key/values pairs in the parameter map and merges them into
     * this.data. parameter values overwrite existing key/values
     * 
     * @param map
     */
    public void setAll(Map<String, String> map) {
        if (map == null) {
            return;
        }
        for (String key : map.keySet()) {
            this.data.put(key, map.get(key));
        }
    }

    /**
     * Convert the object data in JSON format
     * 
     * @return JSONObject representation of this.data
     */
    public JSONObject toJSON() {
        return new JSONObject(this.data);
    }

    /** 
     * Returns a copy of the underlying data Map
     * @return 
     */
    public Map<String, String> toMap() {
        return new HashMap<String, String>(this.data);
    }    
    
    /**
     * Checks is any errors have been set
     * 
     * @returns boolean denoting if there are errors or not
     * 
     */
    public boolean isEmpty() {
        return this.data.isEmpty();
    }

    public void reset() {
        this.data = new HashMap<String, String>();
    }
    
    public void resetTo(Map<String, String> map) {
        if(map == null) { 
            this.data = new HashMap<String, String>();
        } else {
            this.data = map;
        }
    }
    
    // Private Methods
    
    /**
     * Checks if CQ Error data has been set on the request
     */
    public static boolean hasIncomingRequestAttributeData(SlingHttpServletRequest request) {
        if(request.getAttribute(ActiveForm.CQ_FORM_REQUEST_ATTRIBUTE) != null) {
            if(request.getAttribute(ActiveForm.CQ_FORM_REQUEST_ATTRIBUTE) instanceof ActiveForm) {
                ActiveForm form = (ActiveForm) request.getAttribute(ActiveForm.CQ_FORM_REQUEST_ATTRIBUTE);
                return form.hasErrors();
            } 
        }
        
        return false;
    }

    public static boolean hasIncomingQueryParamData(SlingHttpServletRequest request) {            
        RequestParameter param = request.getRequestParameter(CQ_ERRORS);
        if (param == null) {
            return false;
        }
        return (StringUtils.stripToNull(param.getString()) != null);
    }   
}