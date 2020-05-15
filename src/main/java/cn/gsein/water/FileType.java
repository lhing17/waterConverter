package cn.gsein.water;

import cn.gsein.water.converter.factory.ConverterFactory;
import cn.gsein.water.converter.factory.ImageConverterFactory;
import cn.gsein.water.converter.factory.PdfConverterFactory;
import cn.gsein.water.converter.factory.TextConverterFactory;

/**
 * 文件类型枚举
 *
 * @author G. Seinfeld
 * @since 2020-05-13
 */
public enum FileType {
    /**
     * text
     */
    TXT("txt", TextConverterFactory.class),
    /**
     * jpeg
     */
    JPG("jpg", ImageConverterFactory.class),
    /**
     * png
     */
    PNG("png", ImageConverterFactory.class),
    /**
     * bmp
     */
    BMP("bmp", ImageConverterFactory.class),
    /**
     * gif
     */
    GIF("gif", ImageConverterFactory.class),
    /**
     * tiff
     */
    TIFF("tiff", ImageConverterFactory.class),
    /**
     * pdf
     */
    PDF("pdf", PdfConverterFactory.class);

    public Class<? extends ConverterFactory> getConverterFactoryClass() {
        return clazz;
    }

    /**
     * 文件的扩展名
     */
    private final String name;

    /**
     * 转换器工厂的类
     */
    private final Class<? extends ConverterFactory> clazz;

    FileType(String name, Class<? extends ConverterFactory> clazz) {
        this.name = name;
        this.clazz = clazz;
    }

    public String getName() {
        return name;
    }
}
