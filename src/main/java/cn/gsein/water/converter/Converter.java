package cn.gsein.water.converter;

import cn.gsein.water.FileType;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * @author G. Seinfeld
 * @since 2020-05-13
 */
public interface Converter {

    void convert(FileType from, FileType to, InputStream inputStream, String outputPath);

    default void convert(FileType from, FileType to, InputStream inputStream, String outputPath, Properties properties) {
        convert(from, to, inputStream, outputPath);
    }

    default void convert(FileType from, FileType to, File[] inputFiles, String outputPath) throws IOException {
        for (File inputFile : inputFiles) {
            try (InputStream inputStream = new FileInputStream(inputFile)) {
                convert(from, to, inputStream, outputPath);
            }
        }
    }

    default void convert(FileType from, FileType to, File[] inputFiles, String outputPath, Properties properties) throws IOException {
        for (File inputFile : inputFiles) {
            try (InputStream inputStream = new FileInputStream(inputFile)) {
                convert(from, to, inputStream, outputPath, properties);
            }
        }
    }
}
