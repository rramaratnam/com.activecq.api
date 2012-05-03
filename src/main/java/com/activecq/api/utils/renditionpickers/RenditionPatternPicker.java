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
package com.activecq.api.utils.renditionpickers;

import com.day.cq.dam.api.Asset;
import com.day.cq.dam.api.Rendition;
import com.day.cq.dam.api.RenditionPicker;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 * @author david
 */
public class RenditionPatternPicker implements RenditionPicker {

    private final Pattern pattern;

    public RenditionPatternPicker(String pattern) {
        this.pattern = Pattern.compile(pattern);
    }    
    
    public RenditionPatternPicker(Pattern pattern) {
        this.pattern = pattern;
    }

    private Pattern getPattern() {
        return this.pattern;
    }

    @Override
    public Rendition getRendition(Asset asset) {

        List<Rendition> renditions = asset.getRenditions();
        Pattern p = getPattern();

        boolean hasOriginal = asset.getOriginal() != null;
        boolean hasRenditions = renditions.size() > 0;

        for (Rendition rendition : renditions) {
            Matcher m = p.matcher(rendition.getName());

            if (m.find()) {
                return rendition;
            }
        }

        if (hasOriginal) {
            return asset.getOriginal();
        } else if (hasRenditions) {
            return renditions.get(0);
        } else {
            return null;
        }
    }
};