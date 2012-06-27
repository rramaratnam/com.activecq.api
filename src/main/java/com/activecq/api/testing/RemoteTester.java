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
package com.activecq.api.testing;

import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpMethod;
import org.apache.commons.httpclient.UsernamePasswordCredentials;
import org.apache.commons.httpclient.auth.AuthScope;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.lang.StringUtils;
import static org.junit.Assert.*;

/**
 *
 * @author david
 */
public class RemoteTester {

    public static final String REGEX_TRUE = "<!--\\ Begin\\ Test\\ -->((.|\\n)*)false((.|\\n)*)<!--\\ End\\ Test\\ -->";
    
    private final String USERNAME;
    private final String PASSWORD;
    private final String URI;
    private final String METHOD;

    public RemoteTester(String username, String password, String uri, String method) {
        this.USERNAME = username;
        this.PASSWORD = password;
        this.URI = uri;
        this.METHOD = method;
    }

    /**
     *
     * @return
     */
    private HttpClient getClient() {
        HttpClient client = new HttpClient();
        client.getState().setCredentials(AuthScope.ANY,
                new UsernamePasswordCredentials(this.USERNAME, this.PASSWORD));
        return client;
    }

    /**
     *
     * @param selector
     * @return
     */
    private HttpMethod getMethod(String selector, String extension, String queryParams) {
        HttpMethod method;
        String uri = this.URI + "." + selector + "." + extension;
                
        if (StringUtils.equalsIgnoreCase("POST", this.METHOD)) {
            method = new PostMethod(uri);
        } else {
            method = new GetMethod(uri);
        }

        if(StringUtils.stripToNull(queryParams) != null) {
            method.setQueryString(queryParams);
        }    
        
        method.setDoAuthentication(true);
        return method;
    }

    /**
     *
     * @param test
     */
    public void execute(String testName) {
        execute(testName,
                200,
                REGEX_TRUE,
                null);
    }

    public void execute(String testName, String queryParams) {
        execute(testName,
                200,
                REGEX_TRUE,
                queryParams);        
    }
    
    
    public void execute(String testName, int expStatus, String regex, String queryParams) {
        HttpClient client = getClient();
        HttpMethod method = getMethod(testName, "html", queryParams);
        
        System.out.println(">>> " + method.getPath());
        System.out.println(">>>>>> " + method.getQueryString());
        
        try {
            int status = client.executeMethod(method);
            final String response = method.getResponseBodyAsString();

            assertEquals(expStatus, status);

            Pattern p = Pattern.compile(regex);
            Matcher m = p.matcher(response);
            final boolean result = !m.find();

            assertEquals(true, result);

        } catch (Exception ex) {
            Logger.getLogger(RemoteTester.class.getName()).log(Level.SEVERE, null, ex);
            fail("Exception: " + ex.getMessage());
        } finally {
            method.releaseConnection();
        }
    }
}
