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

import java.util.ArrayList;
import java.util.List;
import org.junit.*;
import static org.junit.Assert.*;

/**
 *
 * @author david
 */
public class ActivePropertiesTest {
    
    private class ActivePropertiesStub extends ActiveProperties {
        public static final String VALID_ALL_UPPER = "valid-all-upper";
        public static final String VALID_ALL_UPPER_2 = "valid-all-upper-2";

        public static final String invalid_all_lower = "invalid-all-lower";

        public static final String Invalid_first_letter_upper = "invalid-first-letter-upper";
        public static final String iNVALID_FIRST_LETTER_LOWER = "invalid-first-letter-lower";

        public static final int INT = 10;
    }
    
    public ActivePropertiesTest() {
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
     * Test of getKeys method, of class ActiveProperties.
     */
    @Test
    public void testGetKeys() {
        System.out.println("getKeys");
        ActivePropertiesStub instance = new ActivePropertiesStub();
        List expResult = new ArrayList<String>();
        expResult.add(instance.VALID_ALL_UPPER);
        expResult.add(instance.VALID_ALL_UPPER_2);
        List result = instance.getKeys();
        assertEquals(expResult, result);
    }

    /**
     * Test of size method, of class ActiveProperties.
     */
    @Test
    public void testSize() {
        System.out.println("size");
        ActivePropertiesStub instance = new ActivePropertiesStub();
        int expResult = 2;
        int result = instance.size();
        assertEquals(expResult, result);
    }

    /**
     * Test of length method, of class ActiveProperties.
     */
    @Test
    public void testLength() {
        System.out.println("length");
        ActivePropertiesStub instance = new ActivePropertiesStub();
        int expResult = 2;
        int result = instance.length();
        assertEquals(expResult, result);
    }
}
