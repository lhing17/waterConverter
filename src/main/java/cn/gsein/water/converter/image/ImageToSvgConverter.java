package cn.gsein.water.converter.image;

import cn.gsein.water.FileType;
import cn.gsein.water.util.FileUtils;
import cn.gsein.water.util.PropertyUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.batik.dom.GenericDOMImplementation;
import org.apache.batik.svggen.SVGGraphics2D;
import org.w3c.dom.DOMImplementation;
import org.w3c.dom.Document;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.io.*;
import java.util.Properties;

/**
 * @author G. Seinfeld
 * @since 2020-05-19
 */
@Slf4j
public class ImageToSvgConverter extends ImageToImageConverter {

    private static final String SVG_NAMESPACE = "http://www.w3.org/2000/svg";

    public ImageToSvgConverter() {
        defaultProperties = new Properties();
    }

    @Override
    public void convert(FileType from, FileType to, InputStream inputStream, String outputPath, Properties properties) {
        String name = PropertyUtils.getName(properties, to);
        String fullName = FileUtils.makeDirAndGetFullName(outputPath, name);

        try (FileOutputStream outputStream = new FileOutputStream(fullName);) {
            BufferedImage bufferedImage = ImageIO.read(inputStream);
            ImageStyle imageStyle = new ImageStyle(properties, bufferedImage).invoke();

            imageToSvg(outputStream, bufferedImage, imageStyle);

        } catch (IOException e) {
            log.error(e.getMessage(), e);
        }
    }

    private void imageToSvg(FileOutputStream outputStream, BufferedImage bufferedImage, ImageStyle imageStyle) throws IOException {
        ColorModel colorModel = bufferedImage.getColorModel();
        BufferedImage newBufferedImage;
        if (colorModel.hasAlpha()) {
            newBufferedImage = new BufferedImage(bufferedImage.getWidth(), bufferedImage.getHeight(), BufferedImage.TYPE_INT_ARGB);
        } else {
            newBufferedImage = new BufferedImage(bufferedImage.getWidth(), bufferedImage.getHeight(), BufferedImage.TYPE_INT_RGB);
        }
        newBufferedImage.createGraphics().drawImage(bufferedImage, 0, 0, imageStyle.width, imageStyle.height,
                null);

        DOMImplementation domImpl = GenericDOMImplementation.getDOMImplementation();

        Document document = domImpl.createDocument(SVG_NAMESPACE, FileType.SVG.getName(), null);
        SVGGraphics2D graphics2D = new SVGGraphics2D(document);

        graphics2D.drawImage(newBufferedImage, 0, 0, imageStyle.width, imageStyle.height, null);
        try (Writer writer = new OutputStreamWriter(outputStream)) {
            graphics2D.stream(writer, true);
        }
    }
}
