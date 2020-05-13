package cn.gsein.water.converter;

import java.io.InputStream;
import java.io.OutputStream;

/**
 *
 * @author G. Seinfeld
 * @since 2020-05-13
 */
public interface Converter {

    void convert(InputStream inputStream, OutputStream outputStream);
}
