package cn.gsein.water;

import cn.gsein.water.converter.Converter;
import cn.gsein.water.converter.factory.ConverterFactory;
import cn.gsein.water.util.IOUtils;
import cn.hutool.core.util.StrUtil;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * 统一对外接口
 *
 * @author G. Seinfeld
 * @since 2020-05-13
 */
@Data
@Slf4j
public class Water {
    private FileType from;
    private FileType to;
    private InputStream input;
    private String outputPath;
    private Converter converter;

    public Water(FileType from, FileType to, InputStream input, String outputPath, Converter converter) {
        this.from = from;
        this.to = to;
        this.input = input;
        this.outputPath = outputPath;
        this.converter = converter;
    }

    /**
     * 发起转换
     */
    public void convert() {
        try {
            converter.convert(from, to, input, outputPath);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        } finally {
            IOUtils.closeSilently(input);
        }
    }

    public static String checkSupport() {
        List<String> supportList = new ArrayList<>();

        for (FileType toType : FileType.values()) {
            String supportByToType = checkSupportByToType(toType);
            if (StrUtil.isNotEmpty(supportByToType)) {
                supportList.add(supportByToType);
            }
        }
        return String.join("\n", supportList);
    }

    public static String checkSupportByFromType(FileType fromType) {
        List<String> supportList = new ArrayList<>();
        for (FileType toType : FileType.values()) {
            try {
                ConverterFactory converterFactory = toType.getConverterFactoryClass().getConstructor().newInstance();
                addSupportIfConverterExists(fromType, supportList, toType, converterFactory);
            } catch (Exception ignored) {
            }
        }
        return String.join("\n", supportList);
    }

    public static String checkSupportByToType(FileType toType) {
        List<String> supportList = new ArrayList<>();
        try {
            ConverterFactory converterFactory = toType.getConverterFactoryClass().getConstructor().newInstance();
            for (FileType fromType : FileType.values()) {
                addSupportIfConverterExists(fromType, supportList, toType, converterFactory);
            }
        } catch (Exception ignored) {
        }
        return String.join("\n", supportList);
    }

    private static void addSupportIfConverterExists(FileType fromType, List<String> supportList, FileType toType, ConverterFactory converterFactory) {
        try {
            Converter converter = converterFactory.create(fromType);
            if (converter != null) {
                supportList.add(fromType.getName() + " -> " + toType.getName());
            }
        } catch (Exception ignored) {
        }
    }
}
