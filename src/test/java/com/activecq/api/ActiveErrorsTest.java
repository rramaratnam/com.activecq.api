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

import java.util.HashMap;
import java.util.Map;
import org.apache.sling.commons.json.JSONObject;
import org.junit.*;
import static org.junit.Assert.*;

/**
 *
 * @author david
 */
public class ActiveErrorsTest {
    
    private Map<String, String> map;
    
    public ActiveErrorsTest() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
        
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }
    
    @Before
    public void setUp() {
        map = new HashMap<String, String>();
        map.put("valid-key", "This is the error message 1.");
        map.put("another-valid-key", "This is the error message 2.");
    }
    
    @After
    public void tearDown() {
    }

    /**
     * Test of has method, of class ActiveErrors.
     */
    @Test
    public void testHas() {
        System.out.println("has");
        ActiveErrors instance = new ActiveErrors(map);
        assertTrue(instance.has("valid-key"));
        assertFalse(instance.has("invalid-key"));
    }

    /**
     * Test of hasAny method, of class ActiveErrors.
     */
    @Test
    public void testHasAny() {
        System.out.println("hasAny");
        ActiveErrors instance = new ActiveErrors(map);;
        boolean result = instance.hasAny(new String[] { "invalid-key", "invalid-foo", "valid-key"});
        assertTrue(result);        

        result = instance.hasAny(new String[] { "invalid-key", "invalid-foo", "invalid-bar"});
        assertFalse(result);
    }

    /**
     * Test of hasAll method, of class ActiveErrors.
     */
    @Test
    public void testHasAll() {
        System.out.println("hasAll");
        ActiveErrors instance = new ActiveErrors(map);
        
        boolean result = instance.hasAll(new String[] { "valid-key", "another-valid-key" });
        assertTrue(result);

        result = instance.hasAll(new String[] { "valid-key", "another-valid-key", "invalid-key" });
        assertFalse(result);
    }

    /**
     * Test of get method, of class ActiveErrors.
     */
    @Test
    public void testGet() {
        System.out.println("get");
        ActiveErrors instance = new ActiveErrors(map);
        String expResult = "This is the error message 1.";
        String result = instance.get("valid-key");
        
        assertEquals(expResult, result);
    }

    /**
     * Test of set method, of class ActiveErrors.
     */
    @Test
    public void testSet_String_String() {
        System.out.println("set");
        String key = "new-valid-key";
        String value = "This is a new valid error message";
        ActiveErrors instance = new ActiveErrors(map);
        instance.set(key, value);

        String expResult = value;
        String result = instance.get(key);
        
        assertEquals(expResult, result);        
        assertTrue(instance.has(key));
    }

    /**
     * Test of set method, of class ActiveErrors.
     */
    @Test
    public void testSet_String() {
        System.out.println("set");
        String key = "new-valid-key";
        ActiveErrors instance = new ActiveErrors(map);
        
        instance.set(key);
        String result = instance.get(key);
        
        assertEquals(null, result);                
        assertTrue(instance.has(key));        
    }

    /**
     * Test of setAll method, of class ActiveErrors.
     */
    @Test
    public void testSetAll() {
        System.out.println("setAll");
        
        Map<String, String> newMap = new HashMap<String, String>();
        newMap.put("new-key-1", "Error message 1");
        newMap.put("new-key-2", "Error message 2");
        newMap.put("new-key-3", "Error message 3");
        
        ActiveErrors instance = new ActiveErrors(map);
        
        instance.setAll(newMap);
        
        boolean result = instance.hasAll(new String[] { "new-key-1", "new-key-2", "new-key-3", "valid-key", "another-valid-key" } );
        assertTrue(result);        
    }

    /**
     * Test of toJSON method, of class ActiveErrors.
     */
    @Test
    public void testToJSON() {
        System.out.println("toJSON");
        ActiveErrors instance = new ActiveErrors(map);
        JSONObject expResult = new JSONObject(this.map);
        JSONObject result = instance.toJSON();
        assertEquals(expResult.toString(), result.toString());
    }

    /**
     * Test of toMap method, of class ActiveErrors.
     */
    @Test
    public void testToMap() {
        System.out.println("toMap");
        ActiveErrors instance = new ActiveErrors(map);
        Map expResult = this.map;
        Map result = instance.toMap();
        assertEquals(expResult, result);
    }

    /**
     * Test of isEmpty method, of class ActiveErrors.
     */
    @Test
    public void testIsEmpty() {
        System.out.println("isEmpty");
        ActiveErrors instance = new ActiveErrors(map);
        boolean result = instance.isEmpty();
        assertFalse(result);
    }
}
