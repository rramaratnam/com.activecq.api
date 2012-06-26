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
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.junit.*;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

/**
 *
 * @author david
 */
public class CookieUtilTest {

    private static HttpServletRequest request;    
    private static HttpServletResponse response;
    
    private static Cookie dogCookie;
    private static Cookie catCookie;
    private static Cookie frogCookie;
    private static Cookie tortoiseCookie;

    private static Cookie[] cookies;
    
    public CookieUtilTest() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
                      
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }
    
    @Before
    public void setUp() {
        request = mock(HttpServletRequest.class);
        response = mock(HttpServletResponse.class);
        
        dogCookie = new Cookie("dog-mammal", "woof");
        catCookie = new Cookie("cat-mammal", "meow");
        frogCookie = new Cookie("frog-amphibian", "ribbit");
        tortoiseCookie = new Cookie("tortoise", "...?");
        tortoiseCookie.setMaxAge(100);

        cookies = new Cookie[] { frogCookie, catCookie, tortoiseCookie, dogCookie };     
        
        when(request.getCookies()).thenReturn(cookies);        
        
    }
    
    @After
    public void tearDown() {
    }

    /**
     * Test of addCookie method, of class CookieUtil.
     */
    @Test
    public void testAddCookie() {
        System.out.println("addCookie");
                
        boolean expResult = false;
        boolean result = CookieUtil.addCookie(null, response);
        assertEquals(expResult, result);
        
        expResult = true;
        result = CookieUtil.addCookie(dogCookie, response);
        assertEquals(expResult, result);        
    }

    /**
     * Test of getCookie method, of class CookieUtil.
     */
    @Test
    public void testGetCookie() {
        System.out.println("getCookie");
        
        String cookieName = "snake";                
        Cookie expResult = null;
        Cookie result = CookieUtil.getCookie(request, cookieName);
        assertEquals(expResult, result);
        
        cookieName = "dog-mammal";
        expResult = dogCookie;
        result = CookieUtil.getCookie(request, cookieName);
        assertEquals(expResult, result);
    }

    /**
     * Test of getCookies method, of class CookieUtil.
     */
    @Test
    public void testGetCookies() {
        System.out.println("getCookies");
        
        String regex = "(.*)mammal(.*)";
        List expResult = new ArrayList<Cookie>();
        expResult.add(catCookie);
        expResult.add(dogCookie);
        
        List result = CookieUtil.getCookies(request, regex);
        assertEquals(expResult, result);
    }

    /**
     * Test of extendCookieLife method, of class CookieUtil.
     */
    @Test
    public void testExtendCookieLife() {
        System.out.println("extendCookieLife");

        String cookieName = "tortoise";
        int expiry = 1000;
        boolean expResult = true;
        boolean result = CookieUtil.extendCookieLife(request, response, cookieName, expiry);
        assertEquals(expResult, result);
        
        cookieName = "dodo";        
        expResult = false;
        result = CookieUtil.extendCookieLife(request, response, cookieName, expiry);
        assertEquals(expResult, result);        
    }

    /**
     * Test of dropCookies method, of class CookieUtil.
     */
    @Test
    public void testDropCookies() {
        System.out.println("dropCookies");

        String[] cookieNames = {"dog-mammal", "cat-mammal"};
        CookieUtil.dropCookies(request, response, cookieNames);
        assertTrue(true);
    }

    /**
     * Test of dropCookiesByRegex method, of class CookieUtil.
     */
    @Test
    public void testDropCookiesByRegex() {
        System.out.println("dropCookiesByRegex");

        int expResult = 3;
        int result = CookieUtil.dropCookiesByRegex(request, response, "(.*)mammal(.*)", "^fr(.*)");
        assertEquals(expResult, result);
        
        expResult = 0;
        result = CookieUtil.dropCookiesByRegex(request, response, "nothere");
        assertEquals(expResult, result);        
    }

    /**
     * Test of dropCookiesByRegexArray method, of class CookieUtil.
     */
    @Test
    public void testDropCookiesByRegexArray() {
        System.out.println("dropCookiesByRegexArray");

        String[] regexes = new String[] { "(.*)mammal(.*)", "^fr.*" };
        
        int expResult = 3;        
        int result = CookieUtil.dropCookiesByRegex(request, response, regexes);
        assertEquals(expResult, result);
        
        expResult = 0;
        result = CookieUtil.dropCookiesByRegex(request, response, "nothere");
        assertEquals(expResult, result);        
    }

    /**
     * Test of dropAllCookies method, of class CookieUtil.
     */
    @Test
    public void testDropAllCookies() {
        System.out.println("dropAllCookies");
        
        int expResult = cookies.length;        
        int result = CookieUtil.dropAllCookies(request, response);
        assertEquals(expResult, result);
    }
}
