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

import org.junit.After;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertEquals;
import org.junit.Before;
import org.junit.Test;

/**
 *
 * @author david
 */
public class TextUtilTest {
    
    public TextUtilTest() {
    }
    
    @Before
    public void setUp() {
    }
    
    @After
    public void tearDown() {
    }
    
    
    @Test 
    public void testGetFirstNonNull_String() {
        System.out.println("getFirstNonNull");
        
        String first = null;
        String second = null;
        String third = "third";
        String fourth = null;
                
        String expResult = "third";
        String result = TextUtil.getFirstNonNull(first, second, third, fourth);
        assertEquals(expResult, result); 
    }

    @Test 
    public void testGetFirstNonNull_int() {
        System.out.println("getFirstNonNull");
        
        int first = 1;
        int second = 2;
        int third = 3;
        int fourth = 4;
                
        int expResult = 1;
        int result = TextUtil.getFirstNonNull(first, second, third, fourth);
        assertEquals(expResult, result); 
    }
        
    
    @Test 
    public void testGetFirstNonEmpty() {
        System.out.println("getFirstNonEmpty");
        
        String first = null;
        String second = "";
        String third = "third";
        String fourth = "fourth";
                
        String expResult = "third";
        String result = TextUtil.getFirstNonEmpty(first, second, third, fourth);
        assertEquals(expResult, result);        
    } 
    
    @Test 
    public void testisRichText_PlainText() {
        System.out.println("isRichText - PlainText");
        
        boolean expResult = false;
        boolean result = TextUtil.isRichText("This is is not rich text");
        assertEquals(expResult, result);        
    } 
    
    @Test 
    public void testisRichText_OpenCloseTag() {
        System.out.println("isRichText - OpenCloseTag");
        
        boolean expResult = true;
        boolean result = TextUtil.isRichText("<strong>This is strong text</strong>");
        assertEquals(expResult, result);        
    }    
    
    
    @Test
    public void testIsEmpty_EmptyString() {
        System.out.println("isEmpty - Empty String");
        
        boolean expResult = true;        
        
        assertEquals(expResult, TextUtil.isEmpty(""));          
        assertEquals(expResult, TextUtil.isEmpty("  "));          
        assertEquals(expResult, TextUtil.isEmpty(null));                  
    }
}
