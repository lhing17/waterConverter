package cn.gsein.water;

import org.junit.jupiter.api.Test;

import java.util.Properties;

/**
 * @author G. Seinfeld
 * @since 2020-05-15
 */
class ImageWaterTest {

    @Test
    void noAlphaImageToAlphaImage() {
        Properties properties = new Properties();
        properties.setProperty("whiteToAlpha", "true");

        Water water = new WaterBuilder()
                .from(FileType.JPG)
                .to(FileType.PNG)
                .source("C:/file/a.jpg")
                .target("C:/file")
                .config(properties)
                .build();
        water.convert();
    }

    @Test
    void textToImage() {
        Water water = new WaterBuilder()
                .from(FileType.TXT)
                .to(FileType.SVG)
                .source("C:/file/a.txt")
                .target("C:/file")
                .build();
        water.convert();
    }
}
