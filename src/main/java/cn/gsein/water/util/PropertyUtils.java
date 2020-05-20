package cn.gsein.water.util;

import cn.gsein.water.FileType;

import java.util.Properties;

/**
 * @author G. Seinfeld
 * @since 2020-05-20
 */
public final class PropertyUtils {
    private PropertyUtils(){}

    private static final String DOT = ".";

    public static String getName(Properties properties, FileType to) {
        String baseName = properties.getProperty("name", String.valueOf(System.currentTimeMillis()));
        return baseName + DOT + to.getName();
    }

}
