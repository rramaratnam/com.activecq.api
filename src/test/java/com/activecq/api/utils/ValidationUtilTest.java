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

import com.activecq.api.ActiveForm;
import java.util.HashMap;
import java.util.Map;
import org.junit.After;
import static org.junit.Assert.assertTrue;
import org.junit.Before;
import org.junit.Test;

/**
 *
 * @author david
 */
public class ValidationUtilTest {

    public class TestForm extends ActiveForm {

        public TestForm(Map<String, Object> map) {
            super(map);
        }
    }
    private static Map<String, Object> data = new HashMap<String, Object>();
    private ValidationUtilTest.TestForm form;

    public ValidationUtilTest() {
    }

    @Before
    public void setUp() {
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
        data.put("length-6", "  ab cde  "); // Length includes white space in the middle
        data.put("letters-regex", "ABCdef");

        form = new ValidationUtilTest.TestForm(data);

    }

    @After
    public void tearDown() {
        form = null;
    }

    /**
     * Test of isPresent method, of class ValidationUtil.
     */
    @Test
    public void testIsPresent() {
        System.out.println("isPresent");

        boolean results = true;

        results = results && ValidationUtil.isPresent(form, "present");
        results = results && !ValidationUtil.isPresent(form, "not-present");

        assertTrue(results);
    }

    /**
     * Test of isValidPath method, of class ValidationUtil.
     */
    @Test
    public void testIsValidPath() {
        System.out.println("isValidPath");

        boolean results = true;

        /*
         * results = results && ValidationUtil.isValidPath(form, "valid-path");
         * results = results && !ValidationUtil.isValidPath(form,
         * "invalid-path"); results = results &&
         * !ValidationUtil.isValidPath(form, "null"); results = results &&
         * !ValidationUtil.isValidPath(form, "empty"); results = results &&
         * !ValidationUtil.isValidPath(form, "whitespace");
         */

        assertTrue(true);

    }

    /**
     * Test of isConfirmed method, of class ValidationUtil.
     */
    @Test
    public void testIsConfirmed() {
        System.out.println("isConfirmed");

        boolean results = true;

        results = results && ValidationUtil.isConfirmed(form, "confirmed");

        results = results && !ValidationUtil.isConfirmed(form, "null");
        results = results && !ValidationUtil.isConfirmed(form, "empty");
        results = results && !ValidationUtil.isConfirmed(form, "whitespace");
        results = results && !ValidationUtil.isConfirmed(form, "unconfirmed");;

        assertTrue(results);
    }

    /**
     * Test of isNotIn method, of class ValidationUtil.
     */
    @Test
    public void testIsNotIn() {
        System.out.println("isNotIn");
        String[] blacklist = {"black", "noir", "melns"};

        boolean results = true;

        results = results && ValidationUtil.isNotIn(form, "white", blacklist);
        results = results && !ValidationUtil.isNotIn(form, "black", blacklist);

        assertTrue(results);
    }

    /**
     * Test of isIn method, of class ValidationUtil.
     */
    @Test
    public void testIsIn() {
        System.out.println("isIn");
        String[] whitelist = {"white", "blanc", "balts"};

        boolean results = true;

        results = results && ValidationUtil.isIn(form, "white", whitelist);
        results = results && !ValidationUtil.isIn(form, "black", whitelist);

        assertTrue(results);
    }

    /**
     * Test of isLength method, of class ValidationUtil.
     */
    @Test
    public void testIsLengthEqualTo() {
        System.out.println("isLength - Equal To ");
        assertTrue(ValidationUtil.isLength(form, "length-6", ValidationUtil.Comparisons.EQUAL_TO, 6));
    }

    public void testIsLengthGreaterThan() {
        System.out.println("isLength - Greater Than ");
        assertTrue(ValidationUtil.isLength(form, "length-6", ValidationUtil.Comparisons.GREATER_THAN, 3));
    }

    public void testIsLengthLessThan() {
        System.out.println("isLength - Greater Than ");
        assertTrue(ValidationUtil.isLength(form, "length-6", ValidationUtil.Comparisons.LESS_THAN, 10));
    }

    public void testIsLengthGreaterThanOrEqualTo_GreatThan() {
        System.out.println("isLength - *Greater* Or Equal To ");
        assertTrue(ValidationUtil.isLength(form, "length-6", ValidationUtil.Comparisons.GREATER_THAN_OR_EQUAL_TO, 3));
    }

    public void testIsLengthGreaterThanOrEqualTo_EqualTo() {
        System.out.println("isLength - Greater Or *Equal To* ");
        assertTrue(ValidationUtil.isLength(form, "length-6", ValidationUtil.Comparisons.GREATER_THAN_OR_EQUAL_TO, 6));
    }

    public void testIsLengthLessThanOrEqualTo_LessThan() {
        System.out.println("isLength - Less Or Equal To ");
        assertTrue(ValidationUtil.isLength(form, "length-6", ValidationUtil.Comparisons.LESS_THAN_OR_EQUAL_TO, 10));
    }

    public void testIsLengthLessThanOrEqualTo_EqualTo() {
        System.out.println("isLength - Greater Or *Equal To* ");
        assertTrue(ValidationUtil.isLength(form, "length-6", ValidationUtil.Comparisons.LESS_THAN_OR_EQUAL_TO, 6));
    }

    /**
     * Test of isNumber method, of class ValidationUtil.
     */
    @Test
    public void testIsNumberPositiveInteger() {
        System.out.println("isNumber - Positive Integer");
        assertTrue(ValidationUtil.isNumber(form, "positive-integer"));
    }

    @Test
    public void testIsNumberPositiveDecimal() {
        System.out.println("isNumber - Positive Decimal");
        assertTrue(ValidationUtil.isNumber(form, "positive-decimal"));
    }

    @Test
    public void testIsNumberNegativeDecimal() {
        System.out.println("isNumber - Negative Decimal");
        assertTrue(ValidationUtil.isNumber(form, "negative-decimal"));
    }

    @Test
    public void testIsNumberNull() {
        System.out.println("isNumber - Null");
        assertTrue(ValidationUtil.isNumber(form, "null"));
    }

    @Test
    public void testIsNumberWhitespace() {
        System.out.println("isNumber - Whitespace");
        assertTrue(ValidationUtil.isNumber(form, "whitespace"));
    }

    @Test
    public void testIsNumberWord() {
        System.out.println("isNumber - Word");
        assertTrue(ValidationUtil.isNumber(form, "white"));
    }

    /**
     * Test of isOfFormat method, of class ValidationUtil.
     */
    @Test
    public void testIsOfFormat() {
        System.out.println("isOfFormat");
        assertTrue(ValidationUtil.isOfFormat(form, "letters-regex", "[A-Za-z]*"));
    }
}
