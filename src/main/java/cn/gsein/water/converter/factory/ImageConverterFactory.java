package cn.gsein.water.converter.factory;

import cn.gsein.water.FileType;
import cn.gsein.water.converter.Converter;
import cn.gsein.water.converter.image.ImageToImageConverter;
import cn.gsein.water.converter.image.TextToImageConverter;
import cn.gsein.water.exception.UnsupportedTypeConvertException;

/**
 * @author G. Seinfeld
 * @since 2020-05-13
 */
public class ImageConverterFactory implements ConverterFactory {

    @Override
    public Converter create(FileType from) {
        switch (from) {
            case TEXT:
                return new TextToImageConverter();
            case IMAGE:
                return new ImageToImageConverter();
            default:
                throw new UnsupportedTypeConvertException("暂时不支持从" + from.getName() + "向图片的转换！");
        }


    }
}
