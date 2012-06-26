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
import javax.servlet.http.HttpServletRequest;
import org.junit.*;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

/**
 *
 * @author david
 */
public class HttpRequestUtilTest {
    
    
    public HttpRequestUtilTest() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }
    
    @Before
    public void setUp() {
    }
    
    @After
    public void tearDown() {
    }

    /**
     * Test of getParameterOrAttribute method, of class HttpRequestUtil.
     */
    @Test
    public void testGetParameterOrAttribute_HttpServletRequest_String() {
        System.out.println("getParameterOrAttribute");
        
        HttpServletRequest request = mock(HttpServletRequest.class);
        
        String key = "dog";
        
        String expResult = null;
        String result = HttpRequestUtil.getParameterOrAttribute(request, key);
        assertEquals(expResult, result);
        
        expResult = "bark";
        when(request.getAttribute(key)).thenReturn("bark");
        result = HttpRequestUtil.getParameterOrAttribute(request, key);
        assertEquals(expResult, result);         
        
        expResult = "ruff";
        when(request.getParameter(key)).thenReturn("ruff");
        result = HttpRequestUtil.getParameterOrAttribute(request, key);
        assertEquals(expResult, result);
    }

    /**
     * Test of getParameterOrAttribute method, of class HttpRequestUtil.
     */
    @Test
    public void testGetParameterOrAttribute_withDefault() {
        System.out.println("getParameterOrAttribute");
        
        HttpServletRequest request = mock(HttpServletRequest.class);
        
        String key = "dog";
        String dfault = "woof";
        String expResult = "woof";
        String result = HttpRequestUtil.getParameterOrAttribute(request, key, dfault);
        assertEquals(expResult, result);

        expResult = "bark";
        when(request.getAttribute(key)).thenReturn("bark");
        result = HttpRequestUtil.getParameterOrAttribute(request, key, dfault);
        assertEquals(expResult, result);        
        
        expResult = "ruff";
        when(request.getParameter(key)).thenReturn("ruff");
        result = HttpRequestUtil.getParameterOrAttribute(request, key, dfault);
        assertEquals(expResult, result);
    }

    /**
     * Test of getAttributeOrParameter method, of class HttpRequestUtil.
     */
    @Test
    public void testGetAttributeOrParameter_HttpServletRequest_String() {
        System.out.println("getAttributeOrParameter");
        
        HttpServletRequest request = mock(HttpServletRequest.class);
        
        String key = "dog";
        
        String expResult = null;
        String result = HttpRequestUtil.getAttributeOrParameter(request, key);
        assertEquals(expResult, result);
        
        expResult = "ruff";
        when(request.getParameter(key)).thenReturn("ruff");
        result = HttpRequestUtil.getAttributeOrParameter(request, key);
        assertEquals(expResult, result);

        expResult = "bark";
        when(request.getAttribute(key)).thenReturn("bark");
        result = HttpRequestUtil.getAttributeOrParameter(request, key);
        assertEquals(expResult, result);         
    }

    /**
     * Test of getAttributeOrParameter method, of class HttpRequestUtil.
     */
    @Test
    public void testGetAttributeOrParameter_withDefault() {
        System.out.println("getAttributeOrParameter");
        
        HttpServletRequest request = mock(HttpServletRequest.class);
        
        String key = "dog";
        String dfault = "woof";
        String expResult = "woof";
        String result = HttpRequestUtil.getAttributeOrParameter(request, key, dfault);
        assertEquals(expResult, result);
        
        expResult = "ruff";
        when(request.getParameter(key)).thenReturn("ruff");
        result = HttpRequestUtil.getAttributeOrParameter(request, key, dfault);
        assertEquals(expResult, result);

        expResult = "bark";
        when(request.getAttribute(key)).thenReturn("bark");
        result = HttpRequestUtil.getAttributeOrParameter(request, key, dfault);
        assertEquals(expResult, result);                
    }

    /**
     * Test of hasParameter method, of class HttpRequestUtil.
     */
    @Test
    public void testHasParameter() {
        System.out.println("hasParameter");
        
        HttpServletRequest request = mock(HttpServletRequest.class);                
        String key = "dog";

        when(request.getParameter(key)).thenReturn(null);        
        boolean expResult = false;
        boolean result = HttpRequestUtil.hasParameter(request, key);
        assertEquals(expResult, result);
        
        
        when(request.getParameter(key)).thenReturn("woof");        
        expResult = true;
        result = HttpRequestUtil.hasParameter(request, key);
        assertEquals(expResult, result);                
    }

    /**
     * Test of hasAttribute method, of class HttpRequestUtil.
     */
    @Test
    public void testHasAttribute() {
        System.out.println("hasAttribute");
        
        HttpServletRequest request = mock(HttpServletRequest.class);                
        String key = "dog";

        when(request.getAttribute(key)).thenReturn(null);        
        boolean expResult = false;
        boolean result = HttpRequestUtil.hasAttribute(request, key);
        assertEquals(expResult, result);
        
        
        when(request.getAttribute(key)).thenReturn("woof");        
        expResult = true;
        result = HttpRequestUtil.hasAttribute(request, key);
        assertEquals(expResult, result);  
    }

    /**
     * Test of toString method, of class HttpRequestUtil.
     */
    @Test
    public void testToString() {
        System.out.println("toString");
                
        PathInfo pathInfo = mock(PathInfo.class);
        when(pathInfo.getResourcePath()).thenReturn("/content/testing/page");
        when(pathInfo.getSelectorString()).thenReturn("my.test");
        when(pathInfo.getExtension()).thenReturn("html");
        when(pathInfo.getSuffix()).thenReturn("mock/test.go");       
        
        String expResult = "/content/testing/page.my.test.html/mock/test.go";
        String result = HttpRequestUtil.toString(pathInfo);
        assertEquals(expResult, result);
    }
}
