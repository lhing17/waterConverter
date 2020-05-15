package cn.gsein.water;

import cn.gsein.water.converter.Converter;
import cn.gsein.water.converter.factory.ConverterFactory;
import cn.gsein.water.util.IOUtils;
import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.StrUtil;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
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
    private String inputPath;
    private File inputFile;
    private String[] inputPaths;
    private File[] inputFiles;
    private String outputPath;
    private Converter converter;

    public Water(FileType from, FileType to, InputStream input, String inputPath, File inputFile, String[] inputPaths,
                 File[] inputFiles, String outputPath, Converter converter) {
        this.from = from;
        this.to = to;
        this.input = input;
        this.inputPath = inputPath;
        this.inputFile = inputFile;
        this.inputPaths = inputPaths;
        this.inputFiles = inputFiles;
        this.outputPath = outputPath;
        this.converter = converter;
    }

    /**
     * 发起转换
     */
    public void convert() {
        if (multipleSource()) {
            doMultipleConvert();
        } else {
            doSingleConvert();
        }
    }

    private boolean multipleSource() {
        return ArrayUtil.isNotEmpty(inputFiles) || ArrayUtil.isNotEmpty(inputPaths);
    }

    private void doSingleConvert() {
        try {
            // 转换单个文件
            if (inputFile != null) {
                input = new FileInputStream(inputFile);
            } else if (StrUtil.isNotEmpty(inputPath)) {
                input = new FileInputStream(inputPath);
            }
            if (input != null) {
                converter.convert(from, to, input, outputPath);
            }
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        } finally {
            IOUtils.closeSilently(input);
        }
    }

    private void doMultipleConvert() {
        if (ArrayUtil.isNotEmpty(inputPaths)) {
            inputFiles = new File[inputPaths.length];
            for (int i = 0; i < inputPaths.length; i++) {
                inputFiles[i] = new File(inputPaths[i]);
            }
        }
        if (ArrayUtil.isNotEmpty(inputFiles)) {
            try {
                converter.convert(from, to, inputFiles, outputPath);
            } catch (IOException e) {
                log.error(e.getMessage(), e);
            }
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
