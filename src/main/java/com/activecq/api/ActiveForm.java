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
    protected static final String CQ_FORM_REQUEST_ATTRIBUTE = "_cq_f_req_attr";

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
    public final ActiveProperties Properties;
    /**
     * *************************************************************************
     * ActiveForm Implementation
     * ************************************************************************
     */
    /**
     * Private data modeling attributes
     */
    private Map<String, Object> data = new HashMap<String, Object>();
    private ActiveErrors errors = new ActiveErrors(new HashMap<String, String>());

    protected ActiveForm(ActiveProperties properties) {
        this.Properties = properties;
    }

    /**
     * Constructor to modeling the parameter Resource
     *
     * @param resource
     */
    public ActiveForm(Resource resource) {
        ValueMap map = resource.adaptTo(ValueMap.class);
        this.data = new HashMap<String, Object>(map);
        this.Properties = new ActiveProperties();
    }

    /**
     * Constructor for modeling the data object represented in the parameter Map
     *
     * @param map
     */
    public ActiveForm(Map<String, Object> map) {
        this.data = new HashMap<String, Object>(map);
        this.Properties = new ActiveProperties();
    }

    /**
     * Constructor for modeling the data object represented in the parameter Map
     *
     * @param map
     */
    public ActiveForm(Map<String, Object> map, ActiveProperties properties) {
        for (String key : properties.getKeys()) {
            this.data.put(key, map.get(key));
        }

        this.Properties = properties;
    }

    /**
     * Constructor for modeling the data object submitted via HTTP
     *
     * @param request
     */
    public ActiveForm(SlingHttpServletRequest request, ActiveProperties properties) {
        this.Properties = properties;
        this.data = new HashMap<String, Object>(request.getParameterMap().size());

        for (String key : this.Properties.getKeys()) {
            this.data.put(key, StringUtils.stripToEmpty(request.getParameter(key)));
        }

        if(this.hasIncomingRequestAttributeData(request)) {
            // Get Form and Errors from Request Attr
            // This is to handle when the "Forward" method is used
            final ActiveForm incomingForm = (ActiveForm) request.getAttribute(CQ_FORM_REQUEST_ATTRIBUTE);
            this.errors = new ActiveErrors(request);
            this.data = incomingForm.toMap();            
        } else if (this.hasIncomingQueryParamData(request)) {
            // Get Form and Erros from Query Params
            // This is to handle the "Redirect" method
            String formData = request.getRequestParameter(CQ_FORM).getString();
            this.errors = new ActiveErrors(request);

            if (StringUtils.stripToNull(formData) != null) {
                try {
                    JSONObject jsonForm = new JSONObject(decode(formData));
                    this.data = TypeUtil.toMap(jsonForm);
                } catch (UnsupportedEncodingException e) {
                } catch (JSONException e) {
                }
            }
        }
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

    protected void resetFormTo(Map<String, ? extends Object> map) {
        if (map == null) {
            map = new HashMap<String, String>();
        }

        this.data = (Map<String, Object>) map;
    }

    protected void resetErrorsTo(Map<String, String> map) {
        if (map == null) {
            map = new HashMap<String, String>();
        }

        this.errors = new ActiveErrors(map);
    }

    protected void resetErrorsTo(ActiveErrors errors) {
        this.errors = new ActiveErrors(errors.toMap());
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

    // Form Methods
    /**
     * *
     * Returns the a string of query parameters that hold Form and Form Error
     * data
     *
     * @return
     * @throws JSONException
     * @throws UnsupportedEncodingException
     */
    public String getQueryParameters() throws JSONException,
            UnsupportedEncodingException {

        final String formData = this.toJSON().toString();
        final String errorData = this.getErrors().toJSON().toString();

        String params = ActiveForm.CQ_FORM;
        params += "=";
        params += encode(formData);
        params += "&";
        params += ActiveErrors.CQ_ERRORS;
        params += "=";
        params += encode(errorData);

        return params;
    }

    // Resource Methods
    /**
     * Merge the properties of a Resource into ActiveResource
     */
    public int merge(Resource resource, boolean overwrite) {
        int count = 0;
        ValueMap resourceProperties = resource.adaptTo(ValueMap.class);

        List<String> keys;

        if (Properties != null) {
            keys = Properties.getKeys();
        } else {
            keys = Arrays.asList(this.data.keySet().toArray(new String[0]));
        }

        for (String key : keys) {
            if (resourceProperties.containsKey(key)) {
                if (overwrite || this.isEmpty(key)) {
                    this.set(key, resourceProperties.get(key));
                    count++;
                }
            }
        }

        return count;
    }

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
        return path.concat("?").concat(this.getQueryParameters());
    }

    
    /**
     * *************************************************************************
     * Private Methods
     * *************************************************************************
     */
    /**
     * Checks if CQ Form data has been set on the request
     */
    private boolean hasIncomingRequestAttributeData(SlingHttpServletRequest request) {
        if(request.getAttribute(CQ_FORM_REQUEST_ATTRIBUTE) != null) {
            return (request.getAttribute(CQ_FORM_REQUEST_ATTRIBUTE) instanceof ActiveForm);
        } 
        
        return false;
    }
    
    private boolean hasIncomingQueryParamData(SlingHttpServletRequest request) {
        RequestParameter param = request.getRequestParameter(CQ_FORM);
        if (param == null) {
            return false;
        }
        return (StringUtils.stripToNull(param.getString()) != null);
    }    

    /**
     * *************************************************************************
     * Package Methods
     * *************************************************************************
     */
    static String decode(String encodedData) throws UnsupportedEncodingException {
        final Base64 base64 = new Base64(true);
        final String tmp = base64.decode(encodedData).toString();
        return URLDecoder.decode(tmp, "UTF-8");
    }

    static String encode(String unencodedData) throws UnsupportedEncodingException {
        final Base64 base64 = new Base64(true);
        final String tmp = base64.encodeToString(unencodedData.getBytes());
        return URLEncoder.encode(tmp, "UTF-8");
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


