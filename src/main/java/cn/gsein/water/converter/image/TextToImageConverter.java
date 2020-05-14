package cn.gsein.water.converter.image;


import cn.gsein.water.FileType;
import cn.gsein.water.converter.Converter;
import lombok.extern.slf4j.Slf4j;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
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
public class TextToImageConverter implements Converter {

    Properties defaultProperties;

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

    public static void main(String[] args) throws Exception {
        String message = "“两国交兵，不斩来使”在后世流传下来的交战规则主要只有“两国交兵，不斩来使”。春秋时期诸侯派出的外交使节是不可侵犯的。公元前596年楚国派出申舟出使齐国，楚庄王特意嘱咐不要从宋国经过。宋国执政华元听说了，觉得这是对宋国的莫大侮辱，就设伏击杀死楚国使者。楚庄王为此“投袂而起”，出动大军包围宋国国都整整9个月。宋国派出使者到晋国告急，晋国上一年刚被楚军打败，不敢冒与楚国全面冲突的危险，只是派解扬为使者劝宋国坚守，不要投降。解扬经过郑国，被郑国抓起来交给楚国。楚庄王亲自接见解扬，企图买通他，要他向宋军喊话，说晋军不再提供救援，断绝宋军的希望，解扬不同意。经楚庄王几次威逼利诱，解扬才答应下来。可是当解扬来到了望城中的楼车上，就大声疾呼，说晋国援军不日就到，请宋国无论如何要坚持下去。楚庄王大怒，解扬说：“我答应你的条件只是为了实现使命，现在使命实现了，请立刻处死我。”楚庄王无话可说，反而释放他回晋国。长期围困而无战果，楚庄王打算退兵，可申舟的父亲拦在车前，说：“我儿子不惜生命以完成国王的使命，难道国王要食言了吗？”楚庄王无言以对。申舟父亲建议在宋国建造住房、耕种土地，表示要长期占领宋国，宋国就会表示屈服。宋国见楚军不肯撤退，就派华元为使者来谈判。华元半夜里潜入楚军大营，劫持了楚军统帅子反，说：“我的国君要我为使者来谈判，现在城内确实已是‘易子而食，析骸以爨’，但是如果订立城下之盟则情愿举国牺牲。贵军退到三十里外，我国唯命是听。”子反就在睡床上保证做到。第二天报告了楚庄王，楚军真的退30里外，和宋国停战，双方保证不再互相欺瞒，华元作为这项和约的人质到楚国居住。\n" +
                "后世将这一交战规则称之为“两国交兵，不斩来使”。历史上最著名的战时两国使节以礼相见的故事是“彭城相会”。450年南朝刘宋与北魏发生战争，刘宋发起北伐，先胜后败，战略据点彭城被包围。江夏王刘义恭率领军队死守彭城（今徐州），北魏太武帝想一举打过长江，派出李孝伯为使节进彭城劝降。刘义恭派了张畅为代表与李孝伯谈判。两人都是当时的“名士”，互相代表各自的君主赠送礼品，尽管处在极其残酷的战争环境，但他们在谈判中却仍然是文质彬彬、礼貌周全。这次谈判本身并没有什么实质性的结果，可双方的礼节及言辞，一直被后世誉为战场佳话。";
        String[] strArr = message.split("\n");

        TextToImageConverter converter = new TextToImageConverter();
        converter.createImage(Arrays.asList(strArr), converter.defaultProperties, FileType.JPG, "C:/file/1.zip");
    }


    /**
     * 将字符串按长度切成片段
     *
     * @param source      源字符串
     * @param sliceLength 每个片段的长度
     * @return 切成片段的字符串列表
     */
    private List<String> slice(String source, int sliceLength) {
        List<String> list = new ArrayList<>();
        // 分切片数
        int count = (source.length() - 1) / sliceLength + 1;
        for (int i = 0; i < count; i++) {
            list.add(source.substring(i * sliceLength, Math.min(source.length(), (i + 1) * sliceLength)));
        }
        return list;
    }

    /**
     * 根据str,font的样式等生成图片
     */
    public void createImage(List<String> lines, Properties properties, FileType to, String outputPath) throws Exception {

        // 每个字的边长，标点符号也算一个字
        int characterSideLength = 40;

        int pageWidth = Integer.parseInt(properties.getProperty("width"));
        int pageHeight = Integer.parseInt(properties.getProperty("height"));
        int lineSpace = Integer.parseInt(properties.getProperty("lineSpace"));
        int margin = Integer.parseInt(properties.getProperty("margin"));

        File file = new File(outputPath);
        if (!file.exists()) {
            //noinspection ResultOfMethodCallIgnored
            file.mkdirs();
        }

        String name = String.valueOf(System.currentTimeMillis());
        String fullName = Paths.get(outputPath, name + ".zip").toString();

        Font font = createFontFromProperties(properties);

        // 每行字数
        int charactersPerLine = (pageWidth - 2 * margin - 1) / characterSideLength + 1;

        List<String> lineList = new ArrayList<>();
        for (String str : lines) {
            lineList.addAll(slice(str, charactersPerLine));
        }


        int lineCount = lineList.size();
        int realLineHeight = characterSideLength + lineSpace;

        int linesPerPage = (pageHeight - 2 * margin - 1) / realLineHeight + 1;

        // 生成图片数量
        int imageCount = (lineCount - 1) / linesPerPage + 1;

        try (ZipOutputStream zipOutputStream = new ZipOutputStream(new FileOutputStream(fullName))) {
            for (int i = 0; i < imageCount; i++) {

                zipOutputStream.putNextEntry(new ZipEntry(i + "." + to.getName()));
                // 创建图片
                BufferedImage image = new BufferedImage(pageWidth, pageHeight,
                        BufferedImage.TYPE_INT_RGB);
                Graphics g = image.getGraphics();
                g.setClip(0, 0, pageWidth, pageHeight);
                // 背景色白色
                g.setColor(Color.white);
                g.fillRect(0, 0, pageWidth, pageHeight);
                //  字体颜色黑色
                g.setColor(Color.black);
                // 设置画笔字体
                g.setFont(font);
                // 每张多少行，当到最后一张时判断是否填充满
                for (int j = 0; j < linesPerPage; j++) {
                    int index = j + i * linesPerPage;
                    if (lineList.size() - 1 >= index) {
                        g.drawString(lineList.get(index), margin, margin + realLineHeight * (j + 1));
                    }
                }
                g.dispose();

                // 输出图片
                ImageIO.write(image, to.getName(), zipOutputStream);
            }
        }


    }

    private Font createFontFromProperties(Properties properties) {
        // 字体相关设置
        String fontName = properties.getProperty("fontName");
        int fontSize = Integer.parseInt(properties.getProperty("fontSize"));
        int fontStyle = Integer.parseInt(properties.getProperty("fontStyle"));
        //noinspection MagicConstant
        return new Font(fontName, fontStyle, fontSize);
    }

    @Override
    public void convert(FileType from, FileType to, InputStream inputStream, String outputPath) {

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8));) {

            createImage(reader.lines().collect(Collectors.toList()), defaultProperties, to, outputPath);

        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
    }
}

