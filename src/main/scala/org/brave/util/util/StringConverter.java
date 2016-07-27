package org.brave.util.util;

import org.springframework.core.convert.converter.Converter;

/**
 * @author yuchen
 * @since 2016/7/26.
 */
public class StringConverter implements Converter<String, String> {
    @Override
    public String convert(String source) {
        return source.trim();
    }
}
