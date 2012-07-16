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

import java.util.Arrays;
import java.util.List;
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

    public static final String TEST_BEGIN = "<!-- Begin Test -->";
    public static final String TEST_END = "<!-- End Test -->";
    public static final String REGEX_TEST = "((.|\\n)*)<!--\\ Begin\\ Test\\ -->((.|\\n)*)<!--\\ End\\ Test\\ -->((.|\\n)*)";
    public static final String REGEX_TRUE = "((.|\\n)*)<!--\\ Begin\\ Test\\ -->((.|\\n)*)false((.|\\n)*)<!--\\ End\\ Test\\ -->((.|\\n)*)";
    
    private final String USERNAME;
    private final String PASSWORD;
    private final String SCHEME;
    private final String HOST;
    private final String PATH;
    private final String METHOD;

    public RemoteTester(String username, String password, String scheme, String host, String path, String method) {
        this.USERNAME = username;
        this.PASSWORD = password;
        this.SCHEME = scheme;
        this.HOST = host;
        this.PATH = path;
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
    private HttpMethod getMethod(String path, String selector, String extension, String queryParams) {
        HttpMethod method;
        String tmp = path == null ? this.getURI() : this.getURI(path);
        
        String uri = tmp + "." + selector + "." + extension;
        
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

    public String getURI() {
        String tmp = this.SCHEME + "://" + this.HOST;
        if(StringUtils.startsWith(this.PATH, "/")) {
           return tmp + this.PATH; 
        }
        
        return tmp + "/" + this.PATH;
    }
    
    public String getURI(String path) {
        String tmp = this.SCHEME + "://" + this.HOST;
        if(StringUtils.startsWith(path, "/")) {
           return tmp + path; 
        }
        
        return tmp + "/" + path;
    }    
        
    /**
     *
     * @param test
     */
    public List<String> execute(String testName) {
        return execute(testName,
                200,
                REGEX_TRUE,
                null);
    }

    public List<String> execute(String testName, String queryParams) {
        return execute(testName,
                200,
                REGEX_TRUE,
                queryParams);        
    }

    public List<String> execute(String testName, int expStatus, String regex, String queryParams) {
        return execute(null, testName, expStatus, regex, queryParams);        
    }
        
    public List<String> execute(String path, String testName, int expStatus, String regex, String queryParams) {
        HttpClient client = getClient();
        HttpMethod method = getMethod(null, testName, "html", queryParams);
        
        try {
            int status = client.executeMethod(method);
            assertEquals(expStatus, status);
            
            final String response = method.getResponseBodyAsString();

            System.out.println("Test: " + testName + " > " + status);
            System.out.println("Response: " + response);
            
            assertEquals("No test-block could be found.", true, isTestFormatted(response));            
                        
            String[] results = StringUtils.substringsBetween(response, TEST_BEGIN, TEST_END);
            return Arrays.asList(results);
        } catch (Exception ex) {
            System.out.println("ERROR: " + ex.getMessage());
            Logger.getLogger(RemoteTester.class.getName()).log(Level.SEVERE, null, ex);
            fail("Exception: " + ex.getMessage());
        } finally {
            method.releaseConnection();
        }
        
        return null;
    }    
    
    private boolean isTestFormatted(String data) {
        Pattern p = Pattern.compile(REGEX_TEST, Pattern.MULTILINE);
        Matcher m = p.matcher(data);
        return m.find();        
    }
}
