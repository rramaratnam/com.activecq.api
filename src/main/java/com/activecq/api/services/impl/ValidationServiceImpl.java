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
package com.activecq.api.services.impl;

import com.activecq.api.ActiveForm;
import com.activecq.api.services.ValidationService;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.apache.commons.lang.StringUtils;
import org.apache.felix.scr.annotations.Component;
import org.apache.felix.scr.annotations.Reference;
import org.apache.felix.scr.annotations.Service;
import org.apache.sling.api.resource.*;
import org.osgi.service.component.ComponentContext;

@Component(label = "ActiveCQ - Validation Service",
    description = "Provides standard validations for ActiveResources",
    immediate = true,
    metatype = false)
@Service
public class ValidationServiceImpl implements ValidationService {

    @Reference
    private ResourceResolverFactory resourceResolverFactory;
    private ResourceResolver adminResourceResolver;

    public boolean isPresent(ActiveForm form, String key) {
        if (!form.has(key)) {
            return false;
        }

        Object val = form.get(key, Object.class);
        if (val instanceof String) {
            return StringUtils.stripToNull((String) val) != null;
        } else if (val != null) {
            return true;
        } else {
            return false;
        }
    }

    public boolean isValidPath(ActiveForm form, String key) {
        if (!this.isPresent(form, key)) {
            return false;
        }

        String val = (String) form.get(key);
        Resource r = adminResourceResolver.resolve(val);
        if(r == null) { 
            return false;
        } else if (ResourceUtil.isA(r, NonExistingResource.RESOURCE_TYPE_NON_EXISTING)) {
            return false;
        } else {
            return true;            
        }
    }

    public boolean isConfirmed(ActiveForm form, String key) {
        if (!this.isPresent(form, key)) {
            return false;
        }

        String val = form.get(key);
        return StringUtils.equalsIgnoreCase(val, "true");
    }

    public <T> boolean isNotIn(ActiveForm form, String key, T... blacklist) {
        if (!this.isPresent(form, key)) {
            return false;
        }

        List<T> blist = Arrays.asList(blacklist);
        T val = (T) form.get(key);

        if (val == null) {
            return false;
        }

        for (T item : blist) {
            if (item instanceof Object) {
                if (val.equals(item)) {
                    return false;
                }
            } else {
                if (val == item) {
                    return false;
                }
            }
        }

        return true;
    }

    public <T> boolean isIn(ActiveForm form, String key, T... whitelist) {
        if (!this.isPresent(form, key)) {
            return false;
        }

        List<T> wlist = Arrays.asList(whitelist);
        T val = (T) form.get(key);

        for (T item : wlist) {
            if (item instanceof Object) {
                if (val.equals(item)) {
                    return true;
                }
            } else {
                if (val == item) {
                    return true;
                }
            }
        }

        return false;
    }

    /*
    public <T extends Comparable> boolean isValue(ActiveResource form, String key, T value, Comparisons... comparisons) {
        if (!this.isPresent(form, key)) {
            return false;
        }
        
        boolean result = false;
        
        T val = (T) form.get(key);
        List<Comparisons> comps = Arrays.asList(comparisons);

        for(Comparisons comparison : comps) {
            if(comparison == Comparisons.EQUAL_TO) {
                result = result || (val.equals(value));
            } else if (Comparisons.GREATER_THAN.equals(comparison)) {
                result = result || (val > value);
            } else if (Comparisons.GREATER_THAN_OR_EQUAL_TO.equals(comparison)) {
                result = result || (val >= value);
            } else if (Comparisons.LESS_THAN.equals(comparison)) {
                result = result || (val < value);
            } else if (Comparisons.LESS_THAN_OR_EQUAL_TO.equals(comparison)) {
                result = result || (val <= value);
            } 
        }
        
        return result;
    }    
    */
   
    
    public boolean isLength(ActiveForm form, String key, Comparisons comparison, int length) {
        if (!this.isPresent(form, key)) {
            return false;
        }

        int dataLength = 0;
        Object val = form.get(key);
        
        if(val instanceof String) {
            dataLength = StringUtils.stripToEmpty((String)val).length();
        } else { 
            return false;
        }
        
        if(Comparisons.EQUAL_TO.equals(comparison)) {
          return (dataLength == length);  
        } else if(Comparisons.GREATER_THAN.equals(comparison)) {
          return (dataLength > length);
        } else if(Comparisons.GREATER_THAN_OR_EQUAL_TO.equals(comparison)) {
          return (dataLength >= length);
        } else if(Comparisons.LESS_THAN.equals(comparison)) {
          return (dataLength < length);
        } else if(Comparisons.LESS_THAN_OR_EQUAL_TO.equals(comparison)) {
          return (dataLength <= length);
        } else {
            return false;
        }
    }
        
    public boolean isNumber(ActiveForm form, String key) {
        System.out.println("k: " + key);

        if (!this.isPresent(form, key)) {
            return false;
        }
        
        Object val = form.get(key);
        
        System.out.println(val.toString());
        
        if(val instanceof String) {
            return StringUtils.isNumeric((String)val);
        } else if(val instanceof Integer) {
            return true;            
        } else if(val instanceof Long) {
            return true;            
        } else if(val instanceof Double) {
            return true;
        } else {            
            return false;
        }
    }

    public boolean isOfFormat(ActiveForm form, String key, String regex) {
        if (!this.isPresent(form, key)) {
            return false;
        }
        
        String val = StringUtils.stripToEmpty(form.get(key));
        
        Pattern p = Pattern.compile(regex);
        Matcher m = p.matcher(val);
        
        return m.matches();
    }

    protected void activate(ComponentContext componentContext) {
        try {
            adminResourceResolver = resourceResolverFactory.getAdministrativeResourceResolver(null);
        } catch (LoginException ex) {
            Logger.getLogger(ValidationServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    protected void deactivate(ComponentContext componentContext) {
        if (adminResourceResolver != null) {
            adminResourceResolver.close();
        }
    }
}
