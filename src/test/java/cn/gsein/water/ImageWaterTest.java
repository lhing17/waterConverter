package cn.gsein.water;

import org.junit.jupiter.api.Test;

import java.util.Properties;

import static org.junit.jupiter.api.Assertions.*;

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
}
