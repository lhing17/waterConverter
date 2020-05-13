package cn.gsein.water;

import cn.gsein.water.converter.Converter;
import lombok.Data;

import java.io.InputStream;
import java.io.OutputStream;

/**
 * 统一对外接口
 *
 * @author G. Seinfeld
 * @since 2020-05-13
 */
@Data
public class Water {
    private InputStream input;
    private OutputStream output;
    private Converter converter;

    public Water(InputStream input, OutputStream output, Converter converter) {
        this.input = input;
        this.output = output;
        this.converter = converter;
    }

    public void convert() {
        converter.convert(input, output);
    }
}
