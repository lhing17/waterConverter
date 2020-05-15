package cn.gsein.water.converter;

import cn.gsein.water.FileType;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * 基础转换器
 *
 * @author G. Seinfeld
 * @since 2020-05-15
 */
@Slf4j
public abstract class BaseConverter implements Converter {

    protected Properties defaultProperties;

    @Override
    public void convert(FileType from, FileType to, InputStream inputStream, String outputPath) {
        convert(from, to, inputStream, outputPath, defaultProperties);
    }

    @Override
    public void convert(FileType from, FileType to, File[] inputFiles, String outputPath) {
        convert(from, to, inputFiles, outputPath, defaultProperties);
    }

    @Override
    public void convert(FileType from, FileType to, File[] inputFiles, String outputPath, Properties properties) {
        for (File inputFile : inputFiles) {
            try (InputStream inputStream = new FileInputStream(inputFile)) {
                convert(from, to, inputStream, outputPath, properties);
            } catch (IOException e) {
                log.error(e.getMessage(), e);
            }
        }
    }
}
