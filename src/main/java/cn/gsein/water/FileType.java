package cn.gsein.water;

import cn.gsein.water.converter.factory.ConverterFactory;
import cn.gsein.water.converter.factory.ImageConverterFactory;
import cn.gsein.water.converter.factory.TextConverterFactory;

/**
 * 文件类型
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
    TIFF("tiff", ImageConverterFactory.class);

    public Class<? extends ConverterFactory> getConverterFactoryClass() {
        return clazz;
    }

    private final String name;

    private final Class<? extends ConverterFactory> clazz;

    FileType(String name, Class<? extends ConverterFactory> clazz) {

        this.name = name;
        this.clazz = clazz;
    }

    public String getName() {
        return name;
    }
}
