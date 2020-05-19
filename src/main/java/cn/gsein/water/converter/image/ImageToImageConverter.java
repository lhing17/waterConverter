package cn.gsein.water.converter.image;

import cn.gsein.water.FileType;
import cn.gsein.water.converter.BaseConverter;
import cn.gsein.water.image.CustomImageFilter;
import cn.gsein.water.util.FileUtils;
import lombok.extern.slf4j.Slf4j;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.FilteredImageSource;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * 不同图片格式间相互转换
 *
 * @author G. Seinfeld
 * @since 2020-05-13
 */
@Slf4j
public class ImageToImageConverter extends BaseConverter {

    public ImageToImageConverter() {
        defaultProperties = new Properties();
    }

    @Override
    public void convert(FileType from, FileType to, InputStream inputStream, String outputPath, Properties properties) {

        String name = properties.getProperty("name", String.valueOf(System.currentTimeMillis()));
        String fullName = FileUtils.makeDirAndGetFullName(outputPath, name + "." + to.getName());

        try (FileOutputStream outputStream = new FileOutputStream(fullName)) {
            BufferedImage bufferedImage = ImageIO.read(inputStream);
            ImageStyle imageStyle = new ImageStyle(properties, bufferedImage).invoke();


            ColorModel colorModel = bufferedImage.getColorModel();
            if (colorModel.hasAlpha() && !hasAlpha(to)) {
                // 有alpha转无alpha
                alphaToNoAlpha(to, outputStream, bufferedImage, imageStyle);
            } else if (!colorModel.hasAlpha() && hasAlpha(to)) {
                // 无alpha转有alpha
                noAlphaToAlpha(to, outputStream, bufferedImage, imageStyle);
            } else {
                // alpha相同的情况
                toSameAlpha(to, outputStream, bufferedImage, imageStyle);
            }

        } catch (IOException e) {
            log.error(e.getMessage(), e);
        }
    }

    private void toSameAlpha(FileType to, FileOutputStream outputStream, BufferedImage bufferedImage, ImageStyle imageStyle) throws IOException {
        BufferedImage newBufferedImage;
        if (hasAlpha(to)) {
            newBufferedImage = new BufferedImage(bufferedImage.getWidth(), bufferedImage.getHeight(),
                    BufferedImage.TYPE_INT_ARGB);
            newBufferedImage.createGraphics().drawImage(bufferedImage, 0, 0, imageStyle.width, imageStyle.height,
                    null);
        } else {
            newBufferedImage = new BufferedImage(bufferedImage.getWidth(), bufferedImage.getHeight(),
                    BufferedImage.TYPE_INT_RGB);
            newBufferedImage.createGraphics().drawImage(bufferedImage, 0, 0, imageStyle.width, imageStyle.height,
                    Color.WHITE, null);
        }
        ImageIO.write(newBufferedImage, to.getName(), outputStream);

    }

    private boolean hasAlpha(FileType to) {
        return to == FileType.PNG || to == FileType.TIFF;
    }

    private void alphaToNoAlpha(FileType to, FileOutputStream outputStream, BufferedImage bufferedImage, ImageStyle style) throws IOException {
        BufferedImage newBufferedImage = new BufferedImage(
                bufferedImage.getWidth(), bufferedImage.getHeight(),
                BufferedImage.TYPE_INT_RGB);

        // TYPE_INT_RGB:创建一个RBG图像，24位深度，成功将32位图转化成24位
        newBufferedImage.createGraphics().drawImage(bufferedImage, 0, 0, style.width, style.height,
                Color.WHITE, null);

        // write to specified image file
        ImageIO.write(newBufferedImage, to.getName(), outputStream);
    }

    private void noAlphaToAlpha(FileType to, FileOutputStream outputStream, BufferedImage bufferedImage, ImageStyle style) throws IOException {
        BufferedImage newBufferedImage;
        if (style.whiteToAlpha) {
            newBufferedImage = new BufferedImage(bufferedImage.getWidth(), bufferedImage.getHeight(),
                    BufferedImage.TYPE_INT_ARGB);
            FilteredImageSource imageSource = new FilteredImageSource(bufferedImage.getSource(), new CustomImageFilter());
            Image image = Toolkit.getDefaultToolkit().createImage(imageSource);
            image.flush();

            newBufferedImage.createGraphics().drawImage(image, 0, 0, style.width, style.height, null);

        } else {
            newBufferedImage = new BufferedImage(bufferedImage.getWidth(), bufferedImage.getHeight(),
                    BufferedImage.TYPE_INT_RGB);
            newBufferedImage.createGraphics().drawImage(bufferedImage, 0, 0, style.width, style.height,
                    Color.WHITE, null);
        }
        ImageIO.write(newBufferedImage, to.getName(), outputStream);
    }

    protected static class ImageStyle {
        protected final Properties properties;
        protected final BufferedImage bufferedImage;
        protected int width;
        protected int height;
        // 是否将白色 #FFFFFF 转换为透明
        protected boolean whiteToAlpha;

        public ImageStyle(Properties properties, BufferedImage bufferedImage) {
            this.properties = properties;
            this.bufferedImage = bufferedImage;
        }

        public ImageStyle invoke() {
            width = bufferedImage.getWidth();
            height = bufferedImage.getHeight();
            if (properties.containsKey("width")) {
                width = Integer.parseInt(properties.getProperty("width"));
            }
            if (properties.containsKey("height")) {
                height = Integer.parseInt(properties.getProperty("height"));
            }
            whiteToAlpha = Boolean.parseBoolean(properties.getProperty("whiteToAlpha", "false"));
            return this;
        }
    }
}
