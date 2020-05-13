package cn.gsein.water.converter;

import cn.gsein.water.FileType;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.Properties;

/**
 * @author G. Seinfeld
 * @since 2020-05-13
 */
public interface Converter {

    void convert(FileType from, FileType to, InputStream inputStream, OutputStream outputStream);

    default void convert(FileType from, FileType to, InputStream inputStream, OutputStream outputStream, Properties properties) {
        convert(from, to, inputStream, outputStream);
    }
}
