package cn.gsein.water.converter.image;

import cn.gsein.water.FileType;
import org.apache.batik.dom.GenericDOMImplementation;
import org.apache.batik.svggen.SVGGraphics2D;
import org.w3c.dom.DOMImplementation;
import org.w3c.dom.Document;

import java.awt.image.BufferedImage;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

/**
 * 由文本向SVG转化器
 *
 * @author G. Seinfeld
 * @since 2020-05-18
 */
public class TextToSvgConverter extends TextToImageConverter {

    private static final String SVG_NAMESPACE = "http://www.w3.org/2000/svg";

    @Override
    protected void createOnePageImage(FileType to, ImageStyle imageStyle, String fullName) throws IOException {
        SVGGraphics2D graphics2D = drawSvg(imageStyle, 0);

        try (Writer writer = new OutputStreamWriter(new FileOutputStream(fullName + "." + to.getName()))) {
            graphics2D.stream(writer, true);
        }
    }

    @Override
    protected void createMultiplePageImagesZip(FileType to, ImageStyle imageStyle, String fullName) throws IOException {
        try (ZipOutputStream zipOutputStream = new ZipOutputStream(new FileOutputStream(fullName + ".zip")); Writer writer = new OutputStreamWriter(zipOutputStream)) {
            for (int i = 0; i < imageStyle.imageCount; i++) {
                zipOutputStream.putNextEntry(new ZipEntry(i + "." + to.getName()));
                SVGGraphics2D graphics2D = drawSvg(imageStyle, i);
                graphics2D.stream(writer, true);
            }
        }
    }

    private SVGGraphics2D drawSvg(ImageStyle imageStyle, int i) {
        BufferedImage image = buildBufferedImage(imageStyle, i);

        DOMImplementation domImpl = GenericDOMImplementation.getDOMImplementation();

        Document document = domImpl.createDocument(SVG_NAMESPACE, FileType.SVG.getName(), null);
        SVGGraphics2D graphics2D = new SVGGraphics2D(document);

        graphics2D.drawImage(image, 0, 0, imageStyle.pageWidth, imageStyle.pageHeight, null);
        return graphics2D;
    }
}
