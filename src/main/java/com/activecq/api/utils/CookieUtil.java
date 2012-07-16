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

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.lang.StringUtils;

/**
 *
 * @author david
 */
public class CookieUtil {

    private CookieUtil() { }
    
    public static boolean addCookie(Cookie cookie, HttpServletResponse response) {
        if(cookie == null || response == null) { return false; }
        
        response.addCookie(cookie);
        return true;        
    }
    
    /**
     * Get the named cookie from the HTTP Request
     * 
     * @param request
     * @param cookieName
     * @return
     */
    public static Cookie getCookie(HttpServletRequest request, String cookieName) {
        cookieName = StringUtils.trimToNull(cookieName);
        if (cookieName == null) {
            return null;
        }

        Cookie[] cookies = request.getCookies();
        if(cookies == null) { return null; }
        
        if (cookies.length > 0) {
            for (Cookie cookie : cookies) {
                if (StringUtils.equals(cookieName, cookie.getName())) {
                    return cookie;
                }
            }
        }

        return null;
    }    
    
    public static List<Cookie> getCookies(HttpServletRequest request, String regex) {
        ArrayList<Cookie> foundCookies = new ArrayList<Cookie>();
        regex = StringUtils.trimToNull(regex);
        if (regex == null) {
            return foundCookies;
        }

        Cookie[] cookies = request.getCookies();
        if(cookies == null) { return null; }
        
        for (Cookie cookie : cookies) {
            Pattern p = Pattern.compile(regex);
            Matcher m = p.matcher(cookie.getName());
            if(m.matches()) {
                foundCookies.add(cookie);
            }
        }

        return foundCookies;
    }      
    

    /**
     * Extend the cookie life. 
     * 
     * This can be used when a cookie should be valid for X minutes from the last point of activity.
     * 
     * This method will leave expired or deleted cookies alone.
     * 
     * @param request
     * @param response
     * @param expiry 
     */
    public static boolean extendCookieLife(HttpServletRequest request, HttpServletResponse response, 
            String cookieName, int expiry) {
        Cookie cookie = getCookie(request, cookieName);
        if(cookie == null) { return false; }
        
        if (cookie.getMaxAge() <= 0) {
            return false;
        }
        
        cookie.setMaxAge(expiry);
        cookie.setPath(cookie.getPath());
        
        addCookie(cookie, response);
        
        return true;
    }
        
    
    public static int dropCookies(HttpServletRequest request, HttpServletResponse response, String... cookieNames) {
        int count = 0;
        if(cookieNames == null) { return count; }
        
        for(final String cookieName : cookieNames) {
            Cookie cookie = getCookie(request, cookieName);
            if(cookie == null) { continue; }
            
            cookie.setMaxAge(0);
            cookie.setPath(cookie.getPath());
            
            addCookie(cookie, response);
            count++;
        }
        
        return count;
    }

    public static int dropCookiesByRegex(HttpServletRequest request, HttpServletResponse response, String... regexes) {
        return dropCookiesByRegexArray(request, response, regexes);
    }
    
    public static int dropCookiesByRegexArray(HttpServletRequest request, HttpServletResponse response, String[] regexes) {
        int count = 0;
        if(regexes == null) { return count; }
        List<Cookie> cookies = new ArrayList<Cookie>();
        
        for(final String regex : regexes) {
            cookies.addAll(getCookies(request, regex));
            //cookies = CollectionUtils.union(cookies, getCookies(request, regex));
        }
        
        for(final Cookie cookie : cookies) {
            if(cookie == null) { continue; }
            
            cookie.setMaxAge(0);
            cookie.setPath(cookie.getPath());
            
            addCookie(cookie, response);
            count++;
        }
        
        return count++;
    }    
    
    
    /** 
     * Removes all cookies for the domain
     * 
     * @param request
     * @param response 
     */
    public static int dropAllCookies(HttpServletRequest request, HttpServletResponse response) {
        int count = 0;
        Cookie[] cookies = request.getCookies();
        
        if(cookies == null) { return 0; }
        
        for (Cookie cookie : cookies) {
            cookie.setMaxAge(0);
            cookie.setPath(cookie.getPath());
     
            addCookie(cookie, response);
            count++;
        }
        
        return count;
    }    
}
