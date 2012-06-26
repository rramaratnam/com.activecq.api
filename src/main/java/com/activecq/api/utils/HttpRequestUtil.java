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

import com.day.cq.commons.PathInfo;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import org.apache.commons.lang.StringUtils;

/**
 *
 * @author david
 */
public class HttpRequestUtil {

    public static String getParameterOrAttribute(HttpServletRequest request,String key) {
        return getParameterOrAttribute(request, key, null);
    }
    
    public static String getParameterOrAttribute(HttpServletRequest request,String key, String dfault) {
        String value = null;
        if(request == null) { return value; }
        
        if(hasParameter(request, key)) {
            value = request.getParameter(key);
        } else if (hasAttribute(request, key)) {
            value = (String) request.getAttribute(key);
        }
        
        if(StringUtils.stripToNull(value) == null) {
            value =  dfault;
        }
        
        return value;
    }  
    
    public static String getAttributeOrParameter(HttpServletRequest request,String key) {
        return getAttributeOrParameter(request, key, null);
    }
    
    public static String getAttributeOrParameter(HttpServletRequest request,String key, String dfault) {
        String value = null;
        if(request == null) { return value; }
                
        if (hasAttribute(request, key)) {
            value = (String) request.getAttribute(key);
        } else if(hasParameter(request, key)) {
            value = request.getParameter(key);
        }
        
        if(StringUtils.stripToNull(value) == null) {
            value =  dfault;
        }
        
        return value;
    }     
    
    public static boolean hasParameter(HttpServletRequest request,String key) {
        if(request == null) { return false; }
        String tmp = (String) request.getParameter(key);
        return StringUtils.stripToNull(tmp) != null;        
    }
    
    public static boolean hasAttribute(HttpServletRequest request,String key) {
        if(request == null) { return false; }
        String tmp = (String) request.getAttribute(key);
        return StringUtils.stripToNull(tmp) != null;        
    }
    
    public static String toString(PathInfo pathInfo) {
        if(pathInfo == null) { return ""; }
        
        List<String> tmp = new ArrayList<String>();
        List<String> tmp2 = new ArrayList<String>();
        
        tmp.add(pathInfo.getResourcePath());
        tmp.add(pathInfo.getSelectorString());
        tmp.add(pathInfo.getExtension());
        
        tmp2.add(StringUtils.join(tmp, '.'));
        tmp2.add(pathInfo.getSuffix());
        
        return StringUtils.join(tmp2, '/');
    }
}
