package cn.gsein.water.converter.factory;

import cn.gsein.water.FileType;
import cn.gsein.water.converter.Converter;
import cn.gsein.water.exception.UnsupportedTypeConvertException;

/**
 * 文本文件转换器工厂
 *
 * @author G. Seinfeld
 * @since 2020-05-13
 */
public class TextConverterFactory implements ConverterFactory {
    @Override
    public Converter create(FileType from) {
        throw new UnsupportedTypeConvertException("暂时不支持从" + from.getName() + "向txt的转换！");
    }
}
