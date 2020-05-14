package cn.gsein.water.converter.image;

import cn.gsein.water.FileType;
import cn.gsein.water.converter.Converter;
import lombok.extern.slf4j.Slf4j;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Paths;

/**
 * 不同图片格式间相互转换
 *
 * @author G. Seinfeld
 * @since 2020-05-13
 */
@Slf4j
public class ImageToImageConverter implements Converter {
    @Override
    public void convert(FileType from, FileType to, InputStream inputStream, String outputPath) {
        File file = new File(outputPath);
        if (!file.exists()) {
            //noinspection ResultOfMethodCallIgnored
            file.mkdirs();
        }

        String name = String.valueOf(System.currentTimeMillis());
        String fullName = Paths.get(outputPath, name + "." + to.getName()).toString();

        FileOutputStream outputStream = null;
        try {
            outputStream = new FileOutputStream(fullName);
            BufferedImage bufferedImage = ImageIO.read(inputStream);
            ColorModel colorModel = bufferedImage.getColorModel();

            if (colorModel.hasAlpha() && !hasAlpha(to)) {
                // 有alpha转无alpha
                BufferedImage newBufferedImage = new BufferedImage(
                        bufferedImage.getWidth(), bufferedImage.getHeight(),
                        BufferedImage.TYPE_INT_RGB);

                // TYPE_INT_RGB:创建一个RBG图像，24位深度，成功将32位图转化成24位
                newBufferedImage.createGraphics().drawImage(bufferedImage, 0, 0,
                        Color.WHITE, null);

                // write to jpeg file
                ImageIO.write(newBufferedImage, to.getName(), outputStream);
            } else if (!colorModel.hasAlpha() && hasAlpha(to)) {
                // 无alpha转有alpha
                ImageIO.write(bufferedImage, to.getName(), outputStream);
            } else {
                ImageIO.write(bufferedImage, to.getName(), outputStream);
            }


        } catch (IOException e) {
            log.error(e.getMessage(), e);
        } finally {
            try {
                if (outputStream != null) {
                    outputStream.close();
                }
            } catch (IOException e) {
                log.error(e.getMessage(), e);
            }
        }
    }

    private boolean hasAlpha(FileType to) {
        return to == FileType.PNG;
    }
}
