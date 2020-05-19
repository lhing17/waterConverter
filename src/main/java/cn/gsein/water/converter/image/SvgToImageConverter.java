package cn.gsein.water.converter.image;

import cn.gsein.water.FileType;
import cn.gsein.water.image.svg.CustomImageTranscoder;
import cn.gsein.water.util.FileUtils;
import org.apache.batik.transcoder.TranscoderException;
import org.apache.batik.transcoder.TranscoderInput;
import org.apache.batik.transcoder.TranscoderOutput;
import org.apache.batik.transcoder.image.ImageTranscoder;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * @author G. Seinfeld
 * @since 2020-05-19
 */
public class SvgToImageConverter extends ImageToImageConverter {


    public SvgToImageConverter() {
        defaultProperties = new Properties();
    }

    @Override
    public void convert(FileType from, FileType to, InputStream inputStream, String outputPath, Properties properties) {
        ImageTranscoder transcoder = new CustomImageTranscoder(to);
        if (properties.containsKey("width")) {
            float width = Float.parseFloat(properties.getProperty("width"));
            transcoder.addTranscodingHint(ImageTranscoder.KEY_WIDTH, width);
        }
        if (properties.containsKey("height")) {
            float height = Float.parseFloat(properties.getProperty("height"));
            transcoder.addTranscodingHint(ImageTranscoder.KEY_HEIGHT, height);
        }
        String name = properties.getProperty("name", String.valueOf(System.currentTimeMillis()));
        String fullName = FileUtils.makeDirAndGetFullName(outputPath, name + "." + to.getName());
        try (FileOutputStream outputStream = new FileOutputStream(fullName)) {
            TranscoderInput input = new TranscoderInput(inputStream);
            TranscoderOutput output = new TranscoderOutput(outputStream);
            transcoder.transcode(input, output);
        } catch (IOException | TranscoderException e) {
            e.printStackTrace();
        }
    }
}
