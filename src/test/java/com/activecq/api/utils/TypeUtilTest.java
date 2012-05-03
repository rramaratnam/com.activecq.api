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

import java.util.HashMap;
import java.util.Map;
import org.junit.*;
import static org.junit.Assert.*;

/**
 *
 * @author david
 */
public class TypeUtilTest {
    
    public TypeUtilTest() {
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
     * Test of ArrayToMap method, of class TypeUtil.
     */
    @Test
    public void testArrayToMap() {
        System.out.println("ArrayToMap");
        String[] list = new String[] {"key1", "value1", "key2", "value2", "key3", "value3"};
        Map expResult = new HashMap<String, String>();
        expResult.put("key1", "value1");
        expResult.put("key2", "value2");
        expResult.put("key3", "value3");

        Map result = TypeUtil.ArrayToMap(list);
        assertEquals(expResult, result);
    }
    
    @Test
    public void testArrayToMap_oddNummberArray() {
        System.out.println("ArrayToMap");
        String[] list = new String[] {"key1", "value1", "key2", "value2", "value3"};
        Map expResult = new HashMap<String, String>();
        
        // TODO Add proper JUnit Exception Handling
        try {
            Map result = TypeUtil.ArrayToMap(list);
            assertTrue(false);
        } catch(IllegalArgumentException ex) {
            assertTrue(true);
        }
    }    
}
