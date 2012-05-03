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
package com.activecq.api.test.services;

import com.activecq.api.ActiveForm;
import com.activecq.api.services.ValidationService;
import com.activecq.api.services.ValidationService.Comparisons;
import java.util.HashMap;
import java.util.Map;
import org.apache.sling.junit.annotations.SlingAnnotationsTestRunner;
import org.apache.sling.junit.annotations.TestReference;
import static org.junit.Assert.*;
import org.junit.*;
import org.junit.runner.RunWith;


/**
 *
 * @author david
 */
@RunWith(SlingAnnotationsTestRunner.class)
public class ValidationServiceTest {
    
    @TestReference
    private ValidationService validationService;
    
    private static Map<String, Object> data = new HashMap<String, Object>();
    private TestForm form;
    
    public ValidationServiceTest() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
        data.put("null", null);
        data.put("empty", "");
        data.put("whitespace", "  ");
        data.put("present", "is present");
        data.put("valid-path", "/content");
        data.put("invalid-path", "/does/not/exist");
        data.put("confirmed", "true");
        data.put("unconfirmed", "false");
        data.put("white", "balts");
        data.put("black", "melns");
        data.put("positive-integer", "10");
        data.put("negative-integer", "-10");
        data.put("positive-decimal", "9.05");
        data.put("negative-decimal", "-9.05");
        data.put("length-4", "asdf");
        data.put("length-5", "  asdfg  ");
        data.put("length-6", "  as dfg  ");
        data.put("letters-regex", "ABCdef");
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    
    }
    
    @Before
    public void setUp() {
        form = new TestForm(data);
    }
    
    @After
    public void tearDown() {
        form = null;
    }

    /**
     * Test of isPresent method, of class ValidationService.
     */
    @Test
    public void testIsPresent() {
        System.out.println("isPresent");
        
        boolean results = true;
        
        results = results && validationService.isPresent(form, "present");
        results = results && !validationService.isPresent(form, "not-present");
        
        assertTrue(results);
    }

    /**
     * Test of isValidPath method, of class ValidationService.
     */
    @Test
    public void testIsValidPath() {
        System.out.println("isValidPath");
        
        boolean results = true;
        
        results = results && validationService.isValidPath(form, "valid-path");        
        results = results && !validationService.isValidPath(form, "invalid-path");
        results = results && !validationService.isValidPath(form, "null");
        results = results && !validationService.isValidPath(form, "empty");
        results = results && !validationService.isValidPath(form, "whitespace");
        
        assertTrue(results);
        
    }

    /**
     * Test of isConfirmed method, of class ValidationService.
     */
    @Test
    public void testIsConfirmed() {
        System.out.println("isConfirmed");

        boolean results = true;

        results = results && validationService.isConfirmed(form, "confirmed");

        results = results && !validationService.isConfirmed(form, "null");
        results = results && !validationService.isConfirmed(form, "empty");
        results = results && !validationService.isConfirmed(form, "whitespace");
        results = results && !validationService.isConfirmed(form, "unconfirmed");;
     
        assertTrue(results);        
    }

    /**
     * Test of isNotIn method, of class ValidationService.
     */
    @Test
    public void testIsNotIn() {
        System.out.println("isNotIn");
        String[] blacklist = {"black", "noir", "melns"};

        boolean results = true;
        
        results = results && validationService.isNotIn(form, "white", blacklist);
        results = results && !validationService.isNotIn(form, "black", blacklist);
        
        assertTrue(results);                
    }

    /**
     * Test of isIn method, of class ValidationService.
     */
    @Test
    public void testIsIn() {
        System.out.println("isIn");
        String[] whitelist = {"white", "blanc", "balts"};

        boolean results = true;        
        
        results = results && validationService.isIn(form, "white", whitelist);        
        results = results && !validationService.isIn(form, "black", whitelist);
        
        assertTrue(results);                        
    }

    /**
     * Test of isLength method, of class ValidationService.
     */
    @Test
    public void testIsLength() {
        System.out.println("isLength");
        
        boolean results = true;                
        
        results = results && validationService.isLength(form, "length-4", Comparisons.EQUAL_TO, 4);
        results = results && validationService.isLength(form, "length-5", Comparisons.EQUAL_TO, 5);
        results = results && validationService.isLength(form, "length-6", Comparisons.EQUAL_TO, 6);
        
        assertTrue(results);                        
        
/*
        assertTrue(validationService.isLength(form, "length-4", Comparisons.GREATER_THAN, 3));
        assertTrue(validationService.isLength(form, "length-5", Comparisons.GREATER_THAN, 4));
        assertTrue(validationService.isLength(form, "length-6", Comparisons.GREATER_THAN, 5));

        assertTrue(validationService.isLength(form, "length-4", Comparisons.LESS_THAN, 5));
        assertTrue(validationService.isLength(form, "length-5", Comparisons.LESS_THAN, 6));
        assertTrue(validationService.isLength(form, "length-6", Comparisons.LESS_THAN, 7));

        assertTrue(validationService.isLength(form, "length-4", Comparisons.GREATER_THAN_OR_EQUAL_TO, 3));
        assertTrue(validationService.isLength(form, "length-5", Comparisons.GREATER_THAN_OR_EQUAL_TO, 4));
        assertTrue(validationService.isLength(form, "length-6", Comparisons.GREATER_THAN_OR_EQUAL_TO, 5));
        assertTrue(validationService.isLength(form, "length-4", Comparisons.GREATER_THAN_OR_EQUAL_TO, 4));
        assertTrue(validationService.isLength(form, "length-5", Comparisons.GREATER_THAN_OR_EQUAL_TO, 5));
        assertTrue(validationService.isLength(form, "length-6", Comparisons.GREATER_THAN_OR_EQUAL_TO, 6));

        assertTrue(validationService.isLength(form, "length-4", Comparisons.LESS_THAN_OR_EQUAL_TO, 5));
        assertTrue(validationService.isLength(form, "length-5", Comparisons.LESS_THAN_OR_EQUAL_TO, 6));
        assertTrue(validationService.isLength(form, "length-6", Comparisons.LESS_THAN_OR_EQUAL_TO, 7));
        assertTrue(validationService.isLength(form, "length-4", Comparisons.LESS_THAN_OR_EQUAL_TO, 4));
        assertTrue(validationService.isLength(form, "length-5", Comparisons.LESS_THAN_OR_EQUAL_TO, 5));
        assertTrue(validationService.isLength(form, "length-6", Comparisons.LESS_THAN_OR_EQUAL_TO, 6));
        *
        */
    }

    /**
     * Test of isNumber method, of class ValidationService.
     */
    @Test
    public void testIsNumber() {
        System.out.println("isNumber");

        boolean results = true;        
        
        results = results && validationService.isNumber(form, "positive-integer");
        results = results && validationService.isNumber(form, "negative-integer");
        results = results && validationService.isNumber(form, "positive-decimal");
        results = results && validationService.isNumber(form, "negative-decimal");
        
        results = results && !validationService.isNumber(form, "null");
        results = results && !validationService.isNumber(form, "whitespace");
        results = results && !validationService.isNumber(form, "white");
        results = results && !validationService.isNumber(form, "black");
        
        assertTrue(results);                                
    }

    /**
     * Test of isOfFormat method, of class ValidationService.
     */
    @Test
    public void testIsOfFormat() {
        System.out.println("isOfFormat");

        boolean results = true;        
        
        results = validationService.isOfFormat(form, "letters-regex", "[A-Za-z]*");
        
        assertTrue(results);                        
        
    }
    
    public class TestForm extends ActiveForm {
        public TestForm(Map<String, Object> map) {
            super(map);
        }
    }
}
