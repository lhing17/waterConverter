package cn.gsein.water.converter.factory;

import cn.gsein.water.FileType;
import cn.gsein.water.converter.Converter;
import cn.gsein.water.converter.image.ImageToSvgConverter;
import cn.gsein.water.converter.image.TextToSvgConverter;
import cn.gsein.water.exception.UnsupportedTypeConvertException;

/**
 * SVG格式转换工厂类
 *
 * @author G. Seinfeld
 * @since 2020-05-18
 */
public class SvgConverterFactory extends ImageConverterFactory {

    @Override
    public Converter create(FileType from) {
        switch (from) {
            case TXT:
                return new TextToSvgConverter();
            case JPG:
            case GIF:
            case TIFF:
            case BMP:
            case PNG:
                return new ImageToSvgConverter();
            default:
                throw new UnsupportedTypeConvertException("暂不支持由" + from.getName() + "向SVG的转换");
        }
    }
}
