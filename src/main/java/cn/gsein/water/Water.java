package cn.gsein.water;

import cn.gsein.water.converter.Converter;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.io.InputStream;

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

    public void convert() {
        try {
            converter.convert(from, to, input, outputPath);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        } finally {
            try {
                if (input != null) {
                    input.close();
                }
            } catch (IOException ignored) {
            }
        }
    }
}
