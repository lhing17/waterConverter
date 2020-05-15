package cn.gsein.water;


import cn.gsein.water.converter.Converter;
import cn.gsein.water.converter.factory.ConverterFactory;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.io.InputStream;
import java.util.Properties;

/**
 * Water类的构建器
 *
 * @author G. Seinfeld
 * @since 2020-05-13
 */
@Slf4j
public class WaterBuilder {

    private FileType from;
    private FileType to;
    private InputStream input;
    private String inputPath;
    private File inputFile;
    private String[] inputPaths;
    private File[] inputFiles;
    private String outputPath;
    private Properties properties;

    public WaterBuilder() {
    }

    public WaterBuilder from(FileType from) {
        this.from = from;
        return this;
    }

    public WaterBuilder to(FileType to) {
        this.to = to;
        return this;
    }

    public WaterBuilder source(String source) {
        this.inputPath = source;
        return this;
    }

    public WaterBuilder source(InputStream source) {
        this.input = source;
        return this;
    }

    public WaterBuilder source(File source) {
        this.inputFile = source;
        return this;
    }

    public WaterBuilder source(File... source) {
        this.inputFiles = source;
        return this;
    }

    public WaterBuilder source(String... source) {
        this.inputPaths = source;
        return this;
    }

    public WaterBuilder target(String targetPath) {
        this.outputPath = targetPath;
        return this;
    }

    public WaterBuilder config(Properties properties) {
        this.properties = properties;
        return this;
    }


    public Water build() {
        try {
            ConverterFactory factory = to.getConverterFactoryClass().getConstructor().newInstance();
            Converter converter = factory.create(from);
            return new Water(from, to, input, inputPath, inputFile, inputPaths,
                    inputFiles, outputPath, converter, properties);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return null;
        }

    }

}
