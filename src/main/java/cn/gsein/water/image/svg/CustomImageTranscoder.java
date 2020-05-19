package cn.gsein.water.image.svg;

import cn.gsein.water.FileType;
import cn.gsein.water.exception.ImageWriteException;
import cn.hutool.core.exceptions.ExceptionUtil;
import org.apache.batik.transcoder.TranscoderOutput;
import org.apache.batik.transcoder.image.ImageTranscoder;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.OutputStream;

/**
 * @author G. Seinfeld
 * @since 2020-05-19
 */
public class CustomImageTranscoder extends ImageTranscoder {

    private final FileType to;

    public CustomImageTranscoder(FileType to) {
        this.to = to;
    }

    @Override
    public BufferedImage createImage(int width, int height) {
        if (hasAlpha(to)) {
            return new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        } else {
            return new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        }
    }

    private boolean hasAlpha(FileType to) {
        return to == FileType.PNG || to == FileType.TIFF;
    }

    @Override
    public void writeImage(BufferedImage img, TranscoderOutput output) {
        OutputStream stream = output.getOutputStream();
        try {
            ImageIO.write(img, to.getName(), stream);
        } catch (IOException e) {
            throw ExceptionUtil.wrap(e, ImageWriteException.class);
        }
    }
}
