package cn.gsein.water;

import cn.gsein.water.converter.factory.*;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;

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
     * svg
     */
    SVG("svg", SvgConverterFactory.class),
    /**
     * pdf
     */
    PDF("pdf", PdfConverterFactory.class);

    private static final Map<String, FileType> FILE_TYPE_MAP = new HashMap<>();

    private static final Object LOCK = new Object();
    private static final AtomicBoolean REGISTERED = new AtomicBoolean();

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

    public static FileType getFileTypeByName(String name) {
        if (!REGISTERED.get()) {
            synchronized (LOCK) {
                if (!REGISTERED.get()) {
                    for (FileType type : FileType.values()) {
                        FILE_TYPE_MAP.put(type.getName(), type);
                    }
                    FILE_TYPE_MAP.put("jpeg", FileType.JPG);
                    REGISTERED.set(true);
                }
            }
        }
        return FILE_TYPE_MAP.get(name);
    }

    public String getName() {
        return name;
    }

    public Class<? extends ConverterFactory> getConverterFactoryClass() {
        return clazz;
    }
}
