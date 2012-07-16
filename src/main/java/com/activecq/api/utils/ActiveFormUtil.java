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
package com.activecq.api.utils;

import com.activecq.api.ActiveErrors;
import com.activecq.api.ActiveForm;
import com.day.cq.wcm.api.Page;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang.StringUtils;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.request.RequestParameter;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ValueMap;
import org.apache.sling.commons.json.JSONException;
import org.apache.sling.commons.json.JSONObject;

/**
 *
 * @author david
 */
public class ActiveFormUtil {
    public static <T extends ActiveForm> T load(T form, Resource resource) {
        ValueMap map = resource.adaptTo(ValueMap.class);
        form.resetTo(new HashMap<String, Object>(map));
        
        return form;
    }

    public static <T extends ActiveForm> T load(T form, Map<String, Object> map) {
        form.resetTo(new HashMap<String, Object>(map));
        
        return form;
    }   
        
    public static <T extends ActiveForm> T loadProperties(T form, Map<String, Object> map) {
        for (String key : form.Properties.getKeys()) {
            form.set(key, map.get(key));
        }
        
        return form;
    }         
    
    public static <T extends ActiveForm> T load(T form, SlingHttpServletRequest request) {       
        form.reset();

        for (String key : form.Properties.getKeys()) {
            form.set(key, StringUtils.stripToEmpty(request.getParameter(key)));
        }

        if(hasIncomingRequestAttributeData(request)) {
            // Get Form and Errors from Request Attr
            // This is to handle when the "Forward" method is used
            final ActiveForm incomingForm = (ActiveForm) request.getAttribute(ActiveForm.CQ_FORM_REQUEST_ATTRIBUTE);            
            form.resetTo(incomingForm.toMap());
            form = loadErrorsFromRequest(form, request);
        } else if (hasIncomingQueryParamData(request)) {
            // Get Form and Errors from Query Params
            // This is to handle the "Redirect" method
            String formData = request.getRequestParameter(ActiveForm.CQ_FORM).getString();
            form = loadErrorsFromRequest(form, request);

            if (StringUtils.stripToNull(formData) != null) {
                try {
                    JSONObject jsonForm = new JSONObject(decode(formData));
                    form.resetTo(TypeUtil.toMap(jsonForm));
                } catch (UnsupportedEncodingException e) {
                } catch (JSONException e) {
                }
            }
        }
        
        return form;
    }    
    
    private static <T extends ActiveForm> T loadErrorsFromRequest(T form, SlingHttpServletRequest request) {
        form.getErrors().reset();
        
        if(ActiveErrors.hasIncomingRequestAttributeData(request)) {
            final ActiveForm incomingForm = (ActiveForm) request.getAttribute(ActiveForm.CQ_FORM_REQUEST_ATTRIBUTE);
            if(incomingForm.hasErrors()) {
                form.getErrors().resetTo(incomingForm.getErrors().toMap());
            }            
        } else if (ActiveErrors.hasIncomingQueryParamData(request)) {
            String errorData = null;        
            errorData = request.getRequestParameter(ActiveErrors.CQ_ERRORS).getString();
        
            if(StringUtils.stripToNull(errorData) != null) {
            try {
                    JSONObject jsonErrors = new JSONObject(decode(errorData));
                    form.getErrors().resetTo(TypeUtil.toMap(jsonErrors));
                } catch (UnsupportedEncodingException e) {
                } catch (JSONException e) {
                }
            }
        }   
        
        return form;
    }
    
    
    private static boolean hasIncomingRequestAttributeData(SlingHttpServletRequest request) {
        if(request.getAttribute(ActiveForm.CQ_FORM_REQUEST_ATTRIBUTE) != null) {
            return (request.getAttribute(ActiveForm.CQ_FORM_REQUEST_ATTRIBUTE) instanceof ActiveForm);
        } 
        
        return false;
    }
    
    private static boolean hasIncomingQueryParamData(SlingHttpServletRequest request) {
        RequestParameter param = request.getRequestParameter(ActiveForm.CQ_FORM);
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
    public static String decode(String encodedData) throws UnsupportedEncodingException {
        final Base64 base64 = new Base64(true);
        final String tmp = base64.decode(encodedData).toString();
        return URLDecoder.decode(tmp, "UTF-8");
    }

    public static String encode(String unencodedData) throws UnsupportedEncodingException {
        final Base64 base64 = new Base64(true);
        final String tmp = base64.encodeToString(unencodedData.getBytes());
        return URLEncoder.encode(tmp, "UTF-8");
    }    
    
    /**
     * *
     * Returns the a string of query parameters that hold Form and Form Error
     * data
     *
     * @return
     * @throws JSONException
     * @throws UnsupportedEncodingException
     */
    public static String getQueryParameters(ActiveForm form) throws JSONException,
            UnsupportedEncodingException {

        final String formData = form.toJSON().toString();
        final String errorData = form.getErrors().toJSON().toString();

        String params = ActiveForm.CQ_FORM;
        params += "=";
        params += encode(formData);
        params += "&";
        params += ActiveErrors.CQ_ERRORS;
        params += "=";
        params += encode(errorData);

        return params;
    }
    

    public static String getRedirectPath(ActiveForm form, Page page) throws JSONException,
            UnsupportedEncodingException {
        return getRedirectPath(form, page.adaptTo(Resource.class));
    }

    public static String getRedirectPath(ActiveForm form, Resource resource) throws JSONException,
            UnsupportedEncodingException {
        return getRedirectPath(form, resource.getPath().concat(".html"));
    }

    public static String getRedirectPath(ActiveForm form, String path) throws JSONException,
            UnsupportedEncodingException {
        return path.concat("?").concat(getQueryParameters(form));
    }
   
    
}
