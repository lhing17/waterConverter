package cn.gsein.water.converter.pdf;

import cn.gsein.water.FileType;
import cn.gsein.water.converter.image.SvgToImageConverter;
import cn.gsein.water.exception.ImageWriteException;
import cn.hutool.core.exceptions.ExceptionUtil;
import lombok.extern.slf4j.Slf4j;

import java.io.*;
import java.util.Properties;

/**
 * Svg向PDF转换的转换器
 *
 * @author G. Seinfeld
 * @since 2020/05/23
 */
@Slf4j
public class SvgToPdfConverter extends ImageToPdfConverter {
    private static final String DOT = ".";
    private SvgToImageConverter converter = new SvgToImageConverter();

    @Override
    public void convert(FileType from, FileType to, InputStream inputStream, String outputPath, Properties properties) {
        String tempPath = outputPath + "/temp";
        String alternateName = String.valueOf(System.currentTimeMillis());
        if (!properties.containsKey("name")) {
            properties.setProperty("name", alternateName);
        }
        converter.convert(from, FileType.PNG, inputStream, tempPath, properties);
        String tempFileName = outputPath + "/temp/" + properties.get("name") + DOT + FileType.PNG.getName();
        try (FileInputStream tempInputStream = new FileInputStream(tempFileName)) {
            super.convert(FileType.PNG, to, tempInputStream, outputPath, properties);
        } catch (IOException e) {
            throw ExceptionUtil.wrap(e, ImageWriteException.class);
        } finally {
            File tempFile = new File(tempFileName);
            if (tempFile.exists()) {
                //noinspection ResultOfMethodCallIgnored
                tempFile.delete();
            }
        }

    }

    @Override
    public void convert(FileType from, FileType to, File[] inputFiles, String outputPath, Properties properties) {
        String tempPath = outputPath + "/temp";
        String alternateName = String.valueOf(System.currentTimeMillis());
        if (!properties.containsKey("name")) {
            properties.setProperty("name", alternateName);
        }
        String name = properties.getProperty("name");
        for (int i = 0; i < inputFiles.length; i++) {
            properties.setProperty("name", name + "_" + i);
            try (FileInputStream inputStream = new FileInputStream(inputFiles[i])) {
                converter.convert(from, FileType.PNG, inputStream, tempPath, properties);
            } catch (IOException e) {
                throw ExceptionUtil.wrap(e, ImageWriteException.class);
            }
        }
        File[] tempInputFiles = new File[inputFiles.length];
        for (int i = 0; i < inputFiles.length; i++) {
            tempInputFiles[i] = new File(outputPath + "/temp/" + name + "_" + i + DOT + FileType.PNG.getName());
        }
        try {
            super.convert(FileType.PNG, to,tempInputFiles, outputPath, properties);
        } finally {
            for (File tempInputFile : tempInputFiles) {
                if (tempInputFile.exists()) {
                    //noinspection ResultOfMethodCallIgnored
                    tempInputFile.delete();
                }
            }
        }
    }
}
