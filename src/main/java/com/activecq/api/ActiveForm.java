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

import com.activecq.api.utils.TypeUtil;
import com.day.cq.commons.jcr.JcrConstants;
import com.day.cq.wcm.api.Page;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang.StringEscapeUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.sling.api.SlingConstants;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.request.RequestParameter;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ValueMap;
import org.apache.sling.api.scripting.SlingScriptHelper;
import org.apache.sling.commons.json.JSONException;
import org.apache.sling.commons.json.JSONObject;

public abstract class ActiveForm {

    public static final String CQ_FORM = "_cq_f";
    public static final String CQ_FORM_SUCCESS = "_cq_fs";
    public static final String CQ_FORM_REQUEST_ATTRIBUTE = "_cq_f_req_attr";

    public final FailurePlugin Failure = new FailurePlugin();
    public final SuccessPlugin Success = new SuccessPlugin();
    
    /**
     * ActiveProperties representing system JCR/Sling node properties
     */
    public static class SystemProperties extends ActiveProperties {

        public static final String CREATED_AT = JcrConstants.JCR_CREATED;
        public static final String LAST_MODIFIED_AT = JcrConstants.JCR_LASTMODIFIED;
        public static final String PRIMARY_TYPE = JcrConstants.JCR_PRIMARYTYPE;
        public static final String RESOURCE_TYPE = SlingConstants.PROPERTY_RESOURCE_TYPE;
        public static final String RESOURCE_SUPER_TYPE = SlingConstants.PROPERTY_RESOURCE_SUPER_TYPE;
        public static final String DISTRIBUTE = "cq:distribute";
    }
    public static final SystemProperties SystemProperties = new SystemProperties();

    /**
     * Empty ActiveProperty; ActiveResource objects should extend this
     * Properties (ActiveResource.Properties)
     */
    protected static class Properties extends ActiveProperties {
    }
    //public final ActiveProperties Properties;
    /**
     * *************************************************************************
     * ActiveForm Implementation
     * ************************************************************************
     */
    /**
     * Private data modeling attributes
     */
    private Map<String, Object> data;
    private ActiveErrors errors;

    protected ActiveForm() {
        this.data = new HashMap<String, Object>();
        this.errors = new ActiveErrors();
    }

    /**
     *
     * @param key
     * @return
     */
    public boolean has(String key) {
        return this.data.containsKey(key);
    }

    /**
     * Checks of the value of for a key is empty (null or empty string)
     *
     * @param key
     * @return
     */
    public boolean isEmpty(String key) {
        if (this.has(key)) {
            return StringUtils.stripToNull(this.get(key)) == null;
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
        return this.get(key, "");
    }

    /**
     * Get value from this.data
     *
     * @param <T>
     * @param key
     * @param defaultValue
     * @return
     */
    @SuppressWarnings("unchecked")
    public <T> T get(String key, T defaultValue) {
        if (this.has(key)) {
            return (T) this.data.get(key);
        }

        return defaultValue;
    }

    /**
     * Get value from this.data
     *
     * @param <T>
     * @param key
     * @param type
     * @return
     */
    public <T> T get(String key, Class<T> type) {
        if (this.has(key)) {
            if (type != null) {
                return type.cast(this.data.get(key));
            }
        }

        return null;
    }

    /**
     * Stores a key/value into this.data
     *
     * @param key
     * @param value
     */
    public void set(String key, Object value) {
        this.set(key, value);
    }

    public void set(String key, boolean value) {
        this.set(key, Boolean.valueOf(value));
    }

    public void set(String key, double value) {
        this.set(key, Double.valueOf(value));
    }

    public void set(String key, long value) {
        this.set(key, Long.valueOf(value));
    }

    public void set(String key, int value) {
        this.set(key, Integer.valueOf(value).toString());
    }

    /**
     * Takes all the key/values pairs in the parameter map and merges them into
     * this.data.
     *
     * @param map
     */
    public void setAll(Map<String, ? extends Object> map) {
        if (map == null) {
            return;
        }
        for (String key : map.keySet()) {
            this.data.put(key, map.get(key));
        }
    }

    public void resetTo(Map<String, ? extends Object> map) {
        if (map == null) {
            map = new HashMap<String, String>();
        }

        this.data = (Map<String, Object>) map;
        this.errors = new ActiveErrors();
    }

    public void reset() {
       this.data = new HashMap<String, Object>();
       this.errors = new ActiveErrors();
    }

    /**
     * Checks if any keys have been set
     *
     * @return
     */
    public boolean hasData() {
        return !this.data.isEmpty();
    }

    // Conversion methods
    /**
     * Convert the object data in JSON format
     *
     * @return JSONObject representation of this.data
     */
    public JSONObject toJSON() {
        return new JSONObject(this.data);
    }

    /**
     * Returns this.data at Map object
     *
     * @return Map object representation of this.data (which is already its
     * native data type)
     */
    public Map<String, Object> toMap() {
        return new HashMap<String, Object>(this.data);
    }

    // ActiveError Wrapper/Helper Methods
    /**
     * Gets this' ActiveError object
     *
     * @return ActiveError object
     */
    public ActiveErrors getErrors() {
        return this.errors;
    }

    /**
     * Checks if this' ActiveError object contains any errors
     *
     * @return true if ActiveError object has errors
     */
    public boolean hasErrors() {
        return !this.errors.isEmpty();
    }

    /**
     * Checks if an error has been associated with a specific field (key)
     *
     * @param key
     * @return
     */
    public boolean hasError(String key) {
        return this.errors.has(key);
    }

    /**
     * Gets the error message (String) associated with a specific field (key)
     *
     * @param key
     * @return
     */
    public String getError(String key) {
        if (this.hasError(key)) {
            return this.errors.get(key);
        }

        return null;
    }

    /**
     * Adds an error entry and message to this's ActiveError object
     *
     * @param key
     * @param value
     */
    public void setError(String key, String value) {
        this.errors.set(key, value);
    }

    /**
     * Adds an error entry to this's ActiveError object
     *
     * @param key
     */
    public void setError(String key) {
        this.errors.set(key);
    }
  

    /***************************************************************************
     * 
     * Special ActiveForm Plugin Classes
     * 
     **************************************************************************/

    public class FailurePlugin {
        public String getRedirectPath(Page page) throws JSONException,
                UnsupportedEncodingException {
            return getRedirectPath(page.adaptTo(Resource.class));
        }

        public String getRedirectPath(Resource resource) throws JSONException,
                UnsupportedEncodingException {
            return getRedirectPath(resource.getPath().concat(".html"));
        }

        public String getRedirectPath(String path) throws JSONException,
                UnsupportedEncodingException {
            return path.concat("?").concat(getQueryParameters());
        }        
    
        public void getFormPage(SlingScriptHelper sling, Page page) {
            getFormPage(sling, page.adaptTo(Resource.class));
        }
        
        public void getFormPage(SlingScriptHelper sling, Resource resource) {
            getFormPage(sling, resource.getPath());
        }        
        
        public void getFormPage(SlingScriptHelper sling, String path) {
            sling.getRequest().setAttribute(CQ_FORM_REQUEST_ATTRIBUTE, this);
            sling.forward(path);  
        }
    }
    
    
    public class SuccessPlugin {
        public String getRedirectPath(Page page, String message) throws JSONException,
                UnsupportedEncodingException {
            return getRedirectPath(page.adaptTo(Resource.class), message);
        }

        public String getRedirectPath(Resource resource, String message) throws JSONException,
                UnsupportedEncodingException {
            return getRedirectPath(resource.getPath().concat(".html"), message);
        }

        public String getRedirectPath(String path, String message) throws JSONException,
                UnsupportedEncodingException {
            if(StringUtils.stripToNull(message) == null) {
                return path;
            } else {    
                message = StringEscapeUtils.escapeJavaScript(message);
                return path.concat("?").concat(CQ_FORM_SUCCESS).concat("=").concat(message);                
            }
        }        
    }    
}


