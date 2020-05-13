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
    TEXT("文本", TextConverterFactory.class),
    /**
     * image
     */
    IMAGE("图片", ImageConverterFactory.class);

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
