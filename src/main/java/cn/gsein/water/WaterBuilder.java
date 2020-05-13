package cn.gsein.water;


import cn.gsein.water.converter.Converter;
import cn.gsein.water.converter.factory.ConverterFactory;
import lombok.extern.slf4j.Slf4j;

import java.io.*;

/**
 * @author G. Seinfeld
 * @since 2020-05-13
 */
@Slf4j
public class WaterBuilder {

    private FileType from;
    private FileType to;
    private InputStream input;
    private OutputStream output;

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
        try {
            this.input = new FileInputStream(source);
            return this;
        } catch (FileNotFoundException e) {
            log.error(e.getMessage(), e);
            return null;
        }
    }

    public WaterBuilder source(InputStream source) {
        this.input = source;
        return this;
    }

    public WaterBuilder source(File source) {
        try {
            this.input = new FileInputStream(source);
            return this;
        } catch (FileNotFoundException e) {
            log.error(e.getMessage(), e);
            return null;
        }
    }

    public WaterBuilder target(String target) {
        try {
            this.output = new FileOutputStream(target);
            return this;
        } catch (FileNotFoundException e) {
            log.error(e.getMessage(), e);
            return null;
        }
    }


    public WaterBuilder target(OutputStream target) {
        this.output = target;
        return this;
    }

    public WaterBuilder target(File target) {
        try {
            this.output = new FileOutputStream(target);
            return this;
        } catch (FileNotFoundException e) {
            log.error(e.getMessage(), e);
            return null;
        }
    }

    public Water build() {
        try {
            ConverterFactory factory = to.getConverterFactoryClass().getConstructor().newInstance();
            Converter converter = factory.create(from);
            return new Water(from, to, input, output, converter);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return null;
        }

    }

}
