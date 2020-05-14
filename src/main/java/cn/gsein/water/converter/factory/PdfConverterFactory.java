package cn.gsein.water.converter.factory;

import cn.gsein.water.FileType;
import cn.gsein.water.converter.Converter;
import cn.gsein.water.converter.pdf.TextToPdfConverter;
import cn.gsein.water.exception.UnsupportedTypeConvertException;

/**
 * PDF转换器工厂
 *
 * @author G. Seinfeld
 * @since 2020-05-14
 */
public class PdfConverterFactory implements ConverterFactory {
    @Override
    public Converter create(FileType from) {
        switch (from) {
            case TXT:
                return new TextToPdfConverter();
            case JPG:
            case GIF:
            case TIFF:
            case BMP:
            case PNG:
                return null;
            default:
                throw new UnsupportedTypeConvertException("暂时不支持从" + from.getName() + "向pdf的转换！");
        }
    }
}
