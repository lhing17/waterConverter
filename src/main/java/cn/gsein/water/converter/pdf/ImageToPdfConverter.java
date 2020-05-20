package cn.gsein.water.converter.pdf;

import cn.gsein.water.FileType;
import cn.gsein.water.converter.BaseConverter;
import cn.gsein.water.util.FileUtils;
import cn.gsein.water.util.IOUtils;
import cn.gsein.water.util.PropertyUtils;
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
public class ImageToPdfConverter extends BaseConverter {

    private static final float POINTS_PER_MM = 2.8346457f;

    public ImageToPdfConverter() {
        defaultProperties = new Properties();
        defaultProperties.setProperty("margin", "50");
        defaultProperties.setProperty("pageSize", "A4");
    }

    @Override
    public void convert(FileType from, FileType to, InputStream inputStream, String outputPath, Properties properties) {
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
        createPdf(properties, byteArrayStream, outputPath, to);
    }


    @Override
    public void convert(FileType from, FileType to, File[] inputFiles, String outputPath, Properties properties) {
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
        createPdf(properties, byteArrayStream, outputPath, to);
    }

    public void createPdf(Properties properties, Stream<byte[]> byteArrayStream, String outputPath, FileType to) {
        PDDocument document = new PDDocument();

        File file = new File(outputPath);
        if (!file.exists()) {
            //noinspection ResultOfMethodCallIgnored
            file.mkdirs();
        }

        String name = PropertyUtils.getName(properties, to);
        String fullName = FileUtils.makeDirAndGetFullName(outputPath, name);

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
