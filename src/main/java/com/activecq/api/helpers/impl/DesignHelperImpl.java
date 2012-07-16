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
package com.activecq.api.helpers.impl;

import com.activecq.api.helpers.DesignHelper;
import com.activecq.api.utils.TypeUtil;
import com.day.cq.wcm.api.Page;
import com.day.cq.wcm.api.designer.Designer;
import java.util.Dictionary;
import java.util.HashMap;
import java.util.Map;
import org.apache.commons.lang.StringUtils;
import org.apache.felix.scr.annotations.Component;
import org.apache.felix.scr.annotations.Property;
import org.apache.felix.scr.annotations.Service;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.commons.osgi.OsgiUtil;
import org.osgi.service.component.ComponentContext;

@Component(label = "ActiveCQ - Helpers - Design",
        description = "Sling Service to be used in JSP to allow easier access to Design elements (css, images, js, etc.)",
        metatype = true,
        immediate = true)

@Property(label = "Vendor",
        name = "service.vendor",
        value = "ActiveCQ",
        propertyPrivate = true)
@Service
public class DesignHelperImpl implements DesignHelper {

    private static final String DEFAULT_CSS = "css";    
    private String css = DEFAULT_CSS;
    @Property(label = "CSS Folder Name",
            description = "CSS folder path relative to the Design",
            value = DEFAULT_CSS)
    private static final String PROP_CSS = "prop.folders.css";
    
    
    private static final String DEFAULT_IMAGES = "images";    
    private String images = DEFAULT_IMAGES;    
    @Property(label = "Images Folder Name",
            description = "Images folder path relative to the Design",
            value = DEFAULT_IMAGES)
    private static final String PROP_IMAGES = "prop.folders.images";
    
    private static final String DEFAULT_SCRIPTS = "js";        
    private String scripts = DEFAULT_SCRIPTS;
    @Property(label = "Scripts Folder Name",
            description = "Scripts folder path relative to the Design",
            value = DEFAULT_SCRIPTS)
    private static final String PROP_SCRIPTS = "prop.folders.scripts";

    private static final String DEFAULT_CSS_MEDIA_TYPE = "all";        
    private String cssMediaType = DEFAULT_CSS_MEDIA_TYPE;
    @Property(label = "Default CSS Media Type",
            description = "Default media type when not specified",
            value = DEFAULT_CSS_MEDIA_TYPE)
    private static final String PROP_CSS_MEDIA_TYPE = "prop.css.media-type";

    /*** CSS Methods ***/
    /**
     * Convenience method for calling css without media indicator
     * 
     * @param path
     * @param page
     * @return
     */
    public String cssTag(String path, Page page) {
        return cssTag(path, page, cssMediaType);
    }

    /**
     * Returns an link tag for the css path supplied in the media indicator.
     * 
     * @param path
     * @param page
     * @param attrs
     * @return
     */
    public String cssTag(String path, Page page, String media) {
        String src = cssSrc(path, page);

        if (StringUtils.stripToNull(src) == null) {
            return "<!-- Missing CSS : " + path + ".css -->";
        }

        if (StringUtils.stripToNull(media) == null) {
            media = cssMediaType;
        }

        // Begin writing link tag
        return "<link rel=\"stylesheet\" media=\"" + media + "\" href=\"" + src + "\" />";
    }

    public String cssSrc(String path, Page page) {
        return designSrc(page, css, path);
    }

    /*** Image Methods ***/
    /**
     * Convenience method for calling designImage without extra args
     * 
     * @param path
     * @param page
     * @return
     */
    public String imgTag(String path, Page page) {
        return imgTag(path, page, new HashMap<String, String>());
    }

    /**
     * Returns an img tag for the image path supplied in the path parameter.
     * 
     * @param path
     * @param page
     * @param attrs
     * @return
     */
    public String imgTag(String path, Page page, String[] attrs) {
        Map<String, String> map = new HashMap<String, String>();
        
        try {
            map = TypeUtil.ArrayToMap(attrs);
        } catch (IllegalArgumentException ex) {
            // Pass in empty map
        }
     
        return imgTag(path, page, map);
    }
    
    /**
     * Returns an img tag for the image path supplied in the path parameter.
     * 
     * @param path
     * @param page
     * @param attrs
     * @return
     */
    public String imgTag(String path, Page page, Map<String, String> attrs) {

        String src = imgSrc(path, page);

        if (StringUtils.stripToNull(src) == null) {
            return "<!-- Missing Image : " + path + " -->";
        }

        if (attrs == null) {
            attrs = new HashMap<String, String>();
        }

        // Image alt
        if (!attrs.containsKey("alt")) {
            attrs.put("alt", "");
        }

        // Begin writing img tag
        String html = "<img src=\"" + src + "\"";

        for (String key : attrs.keySet()) {
            html += " " + key + "=\""
                    + StringUtils.stripToEmpty(attrs.get(key)) + "\"";
        }

        html += "/>";

        return html;
    }

    public String imgSrc(String path, Page page) {
        return designSrc(page, images, path);
    }

    /*** Script Methods ***/
    /**
     * Returns an script tag for the script path.
     * 
     * @param path
     * @param page
     * @return
     */
    public String scriptTag(String path, Page page) {
        String src = scriptSrc(path, page);

        if (StringUtils.stripToNull(src) == null) {
            return "<!-- Missing Script : " + path + ".js -->";
        }

        // Begin writing script tag
        return "<script src=\"" + src + "\" ></script>";
    }

    public String scriptSrc(String path, Page page) {
        return designSrc(page, scripts, path);
    }

    /*** SRC Method ***/
    private String designSrc(Page page, String pathPrefix, String path) {
        if (page == null || StringUtils.stripToNull(path) == null) {
            return "";
        }

        // trim leading /'s
        path = StringUtils.removeStart(path, "/");

        Designer designer = page.adaptTo(Resource.class).getResourceResolver().adaptTo(Designer.class);

        if (designer == null) {
            return "";
        }

        final String src = designer.getDesignPath(page) + "/"
                + pathPrefix + "/" + path;

        return (src == null) ? "" : src;
    }

    @SuppressWarnings("unchecked")
    protected void activate(ComponentContext componentContext) {
        Dictionary properties = componentContext.getProperties();

        css = OsgiUtil.toString(properties.get(PROP_CSS), css);

        images = OsgiUtil.toString(properties.get(PROP_IMAGES), DEFAULT_IMAGES);

        scripts = OsgiUtil.toString(properties.get(PROP_SCRIPTS), DEFAULT_SCRIPTS);

        cssMediaType = OsgiUtil.toString(properties.get(PROP_CSS_MEDIA_TYPE), DEFAULT_CSS_MEDIA_TYPE);
    }

    protected void deactivate(ComponentContext componentContext) {
    }
}
