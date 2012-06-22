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

import java.util.Date;
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
        String[] list = new String[]{"key1", "value1", "key2", "value2", "key3", "value3"};
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
        String[] list = new String[]{"key1", "value1", "key2", "value2", "value3"};

        // Expect and exception to be thrown
        try {
            TypeUtil.ArrayToMap(list);
            assertTrue(false);
        } catch (IllegalArgumentException ex) {
            assertTrue(true);
        }
    }

    @Test
    public void testGetType_Double() {
        System.out.println("getType - Double");
        assertEquals(TypeUtil.getType(new Double(10000.00001)), Double.class);
    }

    @Test
    public void testGetType_Float() {
        System.out.println("getType - Float");
        assertEquals(Double.class, TypeUtil.getType(new Float(100.001)));
    }

    @Test
    public void testGetType_Long() {
        System.out.println("getType - Long");
        assertEquals(Long.class, TypeUtil.getType(new Long(100000000)));
    }

    @Test
    public void testGetType_Boolean() {
        System.out.println("getType - Boolean");
        assertEquals(Boolean.class, TypeUtil.getType(Boolean.TRUE));
    }

    @Test
    public void testGetType_Date() {
        System.out.println("getType - Date");
        assertEquals(Date.class, TypeUtil.getType("1997-07-16T19:20:30.450+01:00"));
    }

    @Test
    public void testGetType_String() {
        System.out.println("getType - String");
        assertEquals(String.class, TypeUtil.getType("Hello World!"));
    }

    @Test
    public void toObjectType_Double() {
        System.out.println("toObjectType - Double");
        assertEquals(new Double(10.01), TypeUtil.toObjectType("10.01", Double.class));
    }

    @Test
    public void toObjectType_Long() {
        System.out.println("toObjectType - Long");
        assertEquals(new Long(10), TypeUtil.toObjectType("10", Long.class));
    }

    @Test
    public void toObjectType_BooleanTrue() {
        System.out.println("toObjectType - Boolean True");
        assertEquals(Boolean.TRUE, TypeUtil.toObjectType("true", Boolean.class));
    }

    @Test
    public void toObjectType_BooleanFalse() {
        System.out.println("toObjectType - Boolean False");
        assertEquals(Boolean.FALSE, TypeUtil.toObjectType("false", Boolean.class));
    }
    
    @Test
    public void toObjectType_Date() {
        System.out.println("toObjectType - Date");
        Date expResult = new Date();
        expResult.setTime(1000); // 1 second after the epoch
        assertEquals(expResult, TypeUtil.toObjectType("1970-01-01T00:00:01.000+00:00", Date.class));
    }
    
    @Test
    public void toObjectType_String() {
        System.out.println("toObjectType - String");
        String expResult = "Hello World";
        assertEquals(expResult, TypeUtil.toObjectType(expResult, String.class));
    }   
    
    @Test
    public void toString_Double() {
        System.out.println("toString - Double");
        String expResult = "1000.0";
        Double doubleValue = new Double(1000);
        assertEquals(expResult, TypeUtil.toString(doubleValue, Double.class));        
    }
    
    @Test
    public void toString_Custom() {
        System.out.println("toString - Custom Class");
        String expResult = "Hello World!";
        CustomToString custom = new CustomToString("Hello World");
        assertEquals(expResult, TypeUtil.toString(custom, CustomToString.class, "giveMeAString"));        
    }
    
    /* Custom method for testing */
    private class CustomToString {
        private String val;
        public CustomToString(String val) {
            this.val = val;
        }
        
        public String giveMeAString() {
            return this.val + "!";
        }
    }
    
}
