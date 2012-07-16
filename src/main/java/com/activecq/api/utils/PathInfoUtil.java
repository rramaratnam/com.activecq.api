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

import org.apache.commons.lang.StringUtils;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.request.RequestPathInfo;

/**
 *
 * @author david
 */
public class PathInfoUtil {
    
    private PathInfoUtil() { }
    
    public static String getQueryParam(SlingHttpServletRequest request, String key) {
        return request.getParameter(key);
    }
    
    public static String getQueryParam(SlingHttpServletRequest request, String key, String dfault) {
        String tmp = request.getParameter(key);
        
        if(StringUtils.stripToNull(tmp) == null) {
            return dfault;
        }
        
        return tmp;
    }
    
    public static String getSelector(SlingHttpServletRequest request, int index) {
        RequestPathInfo pathInfo = request.getRequestPathInfo();
        if(pathInfo == null || pathInfo.getSelectors() == null) { return null; }
        
        if(index >= 0 && index < pathInfo.getSelectors().length) {
            return pathInfo.getSelectors()[index];
        } else {
            return null;
        }
    }
    
    
    public static String getSuffixSegment(SlingHttpServletRequest request, int index) {
        RequestPathInfo pathInfo = request.getRequestPathInfo();
        if(pathInfo == null || pathInfo.getSuffix() == null) { return null; }
        
        String []suffixes  = StringUtils.split(pathInfo.getSuffix(), '/');
                        
        if(index >= 0 && index < suffixes.length) {
            return suffixes[index];
        } else {
            return null;
        }
    }    
    
    public static String getSuffix(SlingHttpServletRequest request) {
        RequestPathInfo pathInfo = request.getRequestPathInfo();
        if(pathInfo == null || pathInfo.getSuffix() == null) { return null; }
        
        return pathInfo.getSuffix();
    }      
}
