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
import com.day.cq.commons.SymmetricCrypt;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.Map;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang.StringUtils;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.request.RequestParameter;
import org.apache.sling.commons.json.JSONException;
import org.apache.sling.commons.json.JSONObject;

public class ActiveErrors {

    public static final String CQ_ERRORS = "_e";
    public static final String CQ_ERRORS_ENCRYPTED = ActiveForm.CQ_FORM_ENCRYPTED;

    /**
     * Private data modeling attributes
     */
    protected Map<String, String> data;

    /**
     * Constructor for modeling the data object represented in the parameter Map
     * 
     * @param map
     */
    public ActiveErrors(Map<String, String> map) {
        this.data = new HashMap<String, String>(map);
    }

    /**
     * Constructor for modeling the data object submitted via HTTP
     * 
     * @param request
     */
    @SuppressWarnings("unchecked")
    public ActiveErrors(SlingHttpServletRequest request) {
        this.data = new HashMap<String, String>();

        if (this.hasQueryParamErrorData(request)) {
            // Get query param error data and load into this object            
            RequestParameter encryptedRequestParam = request.getRequestParameter(CQ_ERRORS_ENCRYPTED);
            String encryptedFlag = "false";
            if(encryptedRequestParam != null) {
                encryptedFlag = request.getRequestParameter(CQ_ERRORS_ENCRYPTED).toString();
            }
            
            final boolean isEncrypted = Boolean.valueOf(encryptedFlag);
            
            String errorData = request.getRequestParameter(CQ_ERRORS).getString();
            
            try {
                errorData = URLDecoder.decode(errorData, "UTF-8");

                if (isEncrypted) {
                    final Base64 base64 = new Base64(true);            
                    errorData = base64.decode(errorData).toString();       
                    errorData = SymmetricCrypt.decrypt(errorData);
                }

                JSONObject jsonErrors = new JSONObject(errorData);

                this.data = TypeUtil.toMap(jsonErrors);
            } catch (UnsupportedEncodingException e) {
            } catch (JSONException e) {
            }
        }
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
    public Map<String, Object> toMap() {
        return new HashMap<String, Object>(this.data);
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

    // Private Methods
    
    /**
     * Checks if CQ Error data has been set on the request
     */
    private boolean hasQueryParamErrorData(SlingHttpServletRequest request) {
        RequestParameter param = request.getRequestParameter(CQ_ERRORS);
        if (param == null) {
            return false;
        }
        return (StringUtils.stripToNull(param.getString()) != null);
    }
}