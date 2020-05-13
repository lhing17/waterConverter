package cn.gsein.water.converter.factory;

import cn.gsein.water.FileType;
import cn.gsein.water.converter.Converter;

/**
 * @author G. Seinfeld
 * @since 2020-05-13
 */
public interface ConverterFactory {

    Converter create(FileType from);
}
