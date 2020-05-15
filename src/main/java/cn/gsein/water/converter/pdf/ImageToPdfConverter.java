package cn.gsein.water.converter.pdf;

import cn.gsein.water.FileType;
import cn.gsein.water.converter.Converter;
import cn.gsein.water.util.IOUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Properties;
import java.util.stream.Stream;

/**
 * 图片转PDF工具
 *
 * @author G. Seinfeld
 * @since 2020-05-15
 */
@Slf4j
public class ImageToPdfConverter implements Converter {

    Properties defaultProperties;
    private static final float POINTS_PER_MM = 2.8346457f;

    public ImageToPdfConverter() {
        defaultProperties = new Properties();
        defaultProperties.setProperty("margin", "50");
        defaultProperties.setProperty("pageSize", "A4");
    }

    public static void main(String[] args) {

        String[] fileNames = new String[]{"C:/file/0.png", "C:/file/1.png", "C:/file/2.png", "C:/file/3.png"};
        Stream<byte[]> byteArrayStream = Arrays.stream(fileNames).map(
                fileName -> {
                    try {
                        return Files.readAllBytes(Paths.get(fileName));
                    } catch (IOException e) {
                        log.error(e.getMessage(), e);
                        return null;
                    }
                }
        );
        ImageToPdfConverter converter = new ImageToPdfConverter();
        converter.createPdf(converter.defaultProperties, byteArrayStream, "C:/file");
    }


    @Override
    public void convert(FileType from, FileType to, InputStream inputStream, String outputPath) {
        byte[] byteArray = null;
        try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
            int len;
            byte[] buffer = new byte[1024];
            while ((len = inputStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, len);
            }

            byteArray = outputStream.toByteArray();

        } catch (IOException e) {
            log.error(e.getMessage(), e);
        }
        Stream<byte[]> byteArrayStream = Stream.of(byteArray);
        createPdf(defaultProperties, byteArrayStream, outputPath);
    }

    @Override
    public void convert(FileType from, FileType to, File[] inputFiles, String outputPath) {
        Stream<byte[]> byteArrayStream = Arrays.stream(inputFiles).map(
                inputFile -> {
                    try {
                        return Files.readAllBytes(inputFile.toPath());
                    } catch (IOException e) {
                        log.error(e.getMessage(), e);
                        return null;
                    }
                }
        );
        createPdf(defaultProperties, byteArrayStream, outputPath);
    }

    public void createPdf(Properties properties, Stream<byte[]> byteArrayStream, String outputPath) {
        PDDocument document = new PDDocument();

        File file = new File(outputPath);
        if (!file.exists()) {
            //noinspection ResultOfMethodCallIgnored
            file.mkdirs();
        }

        String name = String.valueOf(System.currentTimeMillis());
        String fullName = Paths.get(outputPath, name + ".pdf").toString();

        byteArrayStream.forEach(
                bytes -> {
                    if (bytes != null) {
                        PDPage page = new PDPage();
                        document.addPage(page);
                        try (PDPageContentStream stream = new PDPageContentStream(document, page)) {
                            PDImageXObject image = PDImageXObject.createFromByteArray(document, bytes, null);
                            float pageWidth = POINTS_PER_MM * 210f;
                            float pageHeight = POINTS_PER_MM * 297f;
                            float x = pageWidth * 0.05f;
                            float y = pageHeight * 0.05f;
                            float width = pageWidth * 0.9f;
                            float height = pageHeight * 0.9f;
                            stream.drawImage(image, x, y, width, height);

                        } catch (IOException e) {
                            log.error(e.getMessage(), e);
                        }
                    }
                }
        );

        try {
            document.save(fullName);
        } catch (IOException e) {
            log.error(e.getMessage(), e);
        } finally {
            IOUtils.closeSilently(document);
        }

    }
}
