package cn.gsein.water.converter.image;


import cn.gsein.water.FileType;
import cn.gsein.water.converter.BaseConverter;
import cn.gsein.water.util.FileUtils;
import cn.gsein.water.util.StringUtils;
import lombok.extern.slf4j.Slf4j;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.stream.Collectors;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

/**
 * 文本到图片的转换器
 *
 * @author G. Seinfeld
 * @since 2020-05-12
 */
@Slf4j
public class TextToImageConverter extends BaseConverter {

    public TextToImageConverter() {
        defaultProperties = new Properties();
        defaultProperties.setProperty("margin", "200");
        defaultProperties.setProperty("width", "1700");
        defaultProperties.setProperty("lineSpace", "30");
        defaultProperties.setProperty("height", "2200");
        defaultProperties.setProperty("fontName", "宋体");
        defaultProperties.setProperty("fontSize", "40");
        defaultProperties.setProperty("fontStyle", String.valueOf(Font.PLAIN));
    }

    @Override
    public void convert(FileType from, FileType to, InputStream inputStream, String outputPath) {
        convert(from, to, inputStream, outputPath, defaultProperties);
    }

    @Override
    public void convert(FileType from, FileType to, InputStream inputStream, String outputPath, Properties properties) {
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8))) {
            createImage(reader.lines().collect(Collectors.toList()), properties, to, outputPath);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
    }

    /**
     * 根据str,font的样式等生成图片
     */
    public void createImage(List<String> lines, Properties properties, FileType to, String outputPath) throws Exception {
        // 每个字的边长，标点符号也算一个字
        ImageStyle imageStyle = new ImageStyle(properties, outputPath, lines).invoke();
        String fullName = imageStyle.getFullName();

        if (imageStyle.imageCount == 1) {
            createOnePageImage(to, imageStyle, fullName);
        } else {
            createMultiplePageImagesZip(to, imageStyle, fullName);
        }

    }

    protected void createOnePageImage(FileType to, ImageStyle imageStyle, String fullName) throws IOException {
        BufferedImage image = buildBufferedImage(imageStyle, 0);
        ImageIO.write(image, to.getName(), new FileOutputStream(fullName + "." + to.getName()));
    }

    protected void createMultiplePageImagesZip(FileType to, ImageStyle imageStyle, String fullName) throws IOException {
        try (ZipOutputStream zipOutputStream = new ZipOutputStream(new FileOutputStream(fullName + ".zip"))) {
            for (int i = 0; i < imageStyle.imageCount; i++) {

                zipOutputStream.putNextEntry(new ZipEntry(i + "." + to.getName()));
                BufferedImage image = buildBufferedImage(imageStyle, i);
                // 输出图片
                ImageIO.write(image, to.getName(), zipOutputStream);
            }
        }
    }

    protected BufferedImage buildBufferedImage(ImageStyle style, int i) {
        // 创建图片
        BufferedImage image = new BufferedImage(style.pageWidth, style.pageHeight, BufferedImage.TYPE_INT_RGB);
        Graphics g = image.getGraphics();
        g.setClip(0, 0, style.pageWidth, style.pageHeight);
        // 背景色白色
        g.setColor(Color.white);
        g.fillRect(0, 0, style.pageWidth, style.pageHeight);
        //  字体颜色黑色
        g.setColor(Color.black);
        // 设置画笔字体
        g.setFont(style.font);
        // 每张多少行，当到最后一张时判断是否填充满
        for (int j = 0; j < style.linesPerPage; j++) {
            int index = j + i * style.linesPerPage;
            if (style.lineList.size() - 1 >= index) {
                g.drawString(style.lineList.get(index), style.margin, style.margin + style.realLineHeight * (j + 1));
            }
        }
        g.dispose();
        return image;
    }

    protected static class ImageStyle {
        private final Properties properties;
        private final String outputPath;
        private final List<String> lines;
        protected int pageWidth;
        protected int pageHeight;
        private int margin;
        private String fullName;
        private Font font;
        protected int imageCount;
        private List<String> lineList;
        private int realLineHeight;
        private int linesPerPage;

        public ImageStyle(Properties properties, String outputPath, List<String> lines) {
            this.properties = properties;
            this.outputPath = outputPath;
            this.lines = lines;
        }

        public String getFullName() {
            return fullName;
        }

        public ImageStyle invoke() {
            int fontSize = Integer.parseInt(properties.getProperty("fontSize", "40"));
            int lineSpace = Integer.parseInt(properties.getProperty("lineSpace", "30"));
            String name = properties.getProperty("name", String.valueOf(System.currentTimeMillis()));
            fullName = FileUtils.makeDirAndGetFullName(outputPath, name);

            pageWidth = Integer.parseInt(properties.getProperty("width", "1700"));
            pageHeight = Integer.parseInt(properties.getProperty("height", "2200"));
            margin = Integer.parseInt(properties.getProperty("margin", "200"));
            font = createFontFromProperties(properties);

            // 每行字数
            int charactersPerLine = (pageWidth - 2 * margin - 1) / fontSize + 1;

            lineList = new ArrayList<>();
            for (String str : lines) {
                lineList.addAll(StringUtils.slice(str, charactersPerLine));
            }


            int lineCount = lineList.size();
            realLineHeight = fontSize + lineSpace;
            linesPerPage = (pageHeight - 2 * margin - 1) / realLineHeight + 1;

            // 生成图片数量
            imageCount = (lineCount - 1) / linesPerPage + 1;
            return this;
        }

        /**
         * 创建字体，需要提供fontName、fontSize和fontStyle属性
         *
         * @param properties 配置项
         * @return 字体
         */
        private Font createFontFromProperties(Properties properties) {
            // 字体相关设置
            String fontName = properties.getProperty("fontName", "宋体");
            int fontSize = Integer.parseInt(properties.getProperty("fontSize", "40"));
            int fontStyle = Integer.parseInt(properties.getProperty("fontStyle", String.valueOf(Font.PLAIN)));

            //noinspection MagicConstant
            return new Font(fontName, fontStyle, fontSize);
        }
    }
}

