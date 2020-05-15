package cn.gsein.water.converter.pdf;

import cn.gsein.water.FileType;
import cn.gsein.water.converter.BaseConverter;
import cn.gsein.water.util.IOUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.fontbox.ttf.TrueTypeCollection;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDFont;
import org.apache.pdfbox.pdmodel.font.PDType0Font;
import org.apache.pdfbox.util.Matrix;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.stream.Collectors;

/**
 * 从文本向PDF转换的转换器
 *
 * @author G. Seinfeld
 * @since 2020-05-14
 */
@Slf4j
public class TextToPdfConverter extends BaseConverter {

    private static final float POINTS_PER_MM = 2.8346457f;

    public TextToPdfConverter() {
        defaultProperties = new Properties();
        defaultProperties.setProperty("margin", "50");
        defaultProperties.setProperty("lineSpace", "0.5");
        defaultProperties.setProperty("pageSize", "A4");
        defaultProperties.setProperty("fontName", "宋体");
        defaultProperties.setProperty("fontSize", "15");
    }

    @Override
    public void convert(FileType from, FileType to, InputStream inputStream, String outputPath, Properties properties) {
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8));) {

            createPdf(reader.lines().collect(Collectors.toList()), properties, outputPath);

        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
    }

    public void createPdf(List<String> lines, Properties properties, String outputPath) {
        PDDocument document = new PDDocument();

        File file = new File(outputPath);
        if (!file.exists()) {
            //noinspection ResultOfMethodCallIgnored
            file.mkdirs();
        }

        String name = String.valueOf(System.currentTimeMillis());
        String fullName = Paths.get(outputPath, name + ".pdf").toString();

        int fontSize = Integer.parseInt(properties.getProperty("fontSize"));
        float lineSpace = Float.parseFloat(properties.getProperty("lineSpace"));
        float pageWidth = POINTS_PER_MM * 210f;
        float pageHeight = POINTS_PER_MM * 297f;
        float lineSpacePoint = lineSpace * fontSize;
        float margin = Float.parseFloat(properties.getProperty("margin"));

        PDFont font;
        try {
            font = PDType0Font.load(document, new TrueTypeCollection(new File("c:/windows/fonts/simsun.ttc")).getFontByName("SimSun"), true);
        } catch (IOException e) {
            log.error(e.getMessage(), e);
            return;
        }

        List<String> lineList = new ArrayList<>();
        for (String str : lines) {
            try {
                if (font.getStringWidth(str.replaceAll("\t", "    ")) < pageWidth - 2 * margin) {
                    lineList.add(str);
                } else {
                    lineList.addAll(slice(str, pageWidth - 2 * margin, font, fontSize));
                }
            } catch (IOException e) {
                log.error(e.getMessage(), e);
            }
        }

        int lineCount = lineList.size();
        float realLineHeight = fontSize + lineSpacePoint;
        int linesPerPage = (int) ((pageHeight - 2 * margin - realLineHeight) / realLineHeight);

        // 生成图片数量
        int pageCount = (lineCount - 1) / linesPerPage + 1;

        for (int i = 0; i < pageCount; i++) {
            PDPage page = new PDPage();
            document.addPage(page);
            PDPageContentStream stream = null;
            try {
                stream = new PDPageContentStream(document, page);


                stream.setFont(font, fontSize);

                stream.beginText();
                for (int j = 0; j < linesPerPage; j++) {
                    int index = j + i * linesPerPage;
                    if (lineList.size() - 1 >= index) {
                        stream.setTextMatrix(Matrix.getTranslateInstance(margin, pageHeight - 2 * margin - realLineHeight * (j + 1) + fontSize * 0.5f));
                        String text = lineList.get(index);
                        text = text.replace("\t", "    ");
                        stream.showText(text);
                    }
                }
                stream.endText();
            } catch (IOException e) {
                log.error(e.getMessage(), e);
            } finally {
                IOUtils.closeSilently(stream);
            }
        }

        try {
            document.save(fullName);
        } catch (IOException e) {
            log.error(e.getMessage(), e);
        } finally {
            IOUtils.closeSilently(document);
        }
    }

    private List<String> slice(String str, float width, PDFont font, float fontSize) throws IOException {
        List<String> list = new ArrayList<>();
        int start = 0;
        for (int i = 1; i < str.length(); i++) {
            String sub = str.substring(start, i);
            if (font.getStringWidth(sub.replaceAll("\t", "    ")) / 1000f * fontSize > width && start < i - 1) {
                list.add(str.substring(start, i - 1));
                start = i - 1;
            }
            if (i == str.length() - 1) {
                list.add(str.substring(start, i + 1));
            }
        }
        return list;
    }
}
