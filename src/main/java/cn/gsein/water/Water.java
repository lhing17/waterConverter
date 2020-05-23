package cn.gsein.water;

import cn.gsein.water.converter.Converter;
import cn.gsein.water.converter.factory.ConverterFactory;
import cn.gsein.water.util.IOUtils;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.StrUtil;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.util.*;

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
    /**
     * 配置项，不同转换器配置项不同
     */
    private Properties properties;

    public Water(FileType from, FileType to, InputStream input, String inputPath, File inputFile, String[] inputPaths,
                 File[] inputFiles, String outputPath, Converter converter, Properties properties) {
        this.from = from;
        this.to = to;
        this.input = input;
        this.inputPath = inputPath;
        this.inputFile = inputFile;
        this.inputPaths = inputPaths;
        this.inputFiles = inputFiles;
        this.outputPath = outputPath;
        this.converter = converter;
        this.properties = properties;
    }

    public static Map<FileType, List<FileType>> checkSupport(List<FileType> fromList, List<FileType> toList) {
        Map<FileType, List<FileType>> supportMap = new HashMap<>();
        List<FileType> internalFromList = CollectionUtil.isEmpty(fromList) ? Arrays.asList(FileType.values()) : fromList;
        List<FileType> internalToList = CollectionUtil.isEmpty(toList) ? Arrays.asList(FileType.values()) : toList;
        for (FileType fromType : internalFromList) {
            List<FileType> supportToListOfFromType = new ArrayList<>();
            for (FileType toType : internalToList) {
                try {
                    ConverterFactory factory = toType.getConverterFactoryClass().getConstructor().newInstance();
                    Converter converter = factory.create(fromType);
                    if (converter != null) {
                        supportToListOfFromType.add(toType);
                    }
                } catch (Exception ignored) {
                }
            }
            supportMap.put(fromType, supportToListOfFromType);
        }
        return supportMap;
    }

    public static String checkAllReadableSupports() {
        Map<FileType, List<FileType>> supportMap = checkSupport(null, null);
        return convertSupportMapToReadableText(supportMap);
    }

    private static String convertSupportMapToReadableText(Map<FileType, List<FileType>> supportMap) {
        StringBuilder builder = new StringBuilder();
        for (Map.Entry<FileType, List<FileType>> entry : supportMap.entrySet()) {
            FileType from = entry.getKey();
            for (FileType to : entry.getValue()) {
                builder.append(from.getName()).append(" -> ").append(to.getName()).append("\n");
            }
        }
        return builder.toString();
    }

    public static String checkReadableSupportsByToTypes(FileType... toTypes) {
        Map<FileType, List<FileType>> supportMap = checkSupport(null, Arrays.asList(toTypes));
        return convertSupportMapToReadableText(supportMap);
    }


    public static String checkReadableSupportsByFromTypes(FileType... fromTypes) {
        Map<FileType, List<FileType>> supportMap = checkSupport(Arrays.asList(fromTypes), null);
        return convertSupportMapToReadableText(supportMap);
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

    /**
     * 是否具有多个源
     */
    private boolean multipleSource() {
        return ArrayUtil.isNotEmpty(inputFiles) || ArrayUtil.isNotEmpty(inputPaths);
    }

    private void doMultipleConvert() {
        // 为输入文件赋值
        if (ArrayUtil.isNotEmpty(inputPaths)) {
            inputFiles = new File[inputPaths.length];
            for (int i = 0; i < inputPaths.length; i++) {
                inputFiles[i] = new File(inputPaths[i]);
            }
        }

        // 进行转换处理
        if (ArrayUtil.isNotEmpty(inputFiles)) {
            if (properties != null) {
                converter.convert(from, to, inputFiles, outputPath, properties);
            } else {
                converter.convert(from, to, inputFiles, outputPath);
            }
        }
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
                if (properties != null) {
                    converter.convert(from, to, input, outputPath, properties);
                } else {
                    converter.convert(from, to, input, outputPath);
                }
            }
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        } finally {
            IOUtils.closeSilently(input);
        }
    }
}
