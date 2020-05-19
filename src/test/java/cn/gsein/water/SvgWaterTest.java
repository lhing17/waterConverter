package cn.gsein.water;

import org.junit.jupiter.api.Test;

import java.util.Properties;

/**
 * @author G. Seinfeld
 * @since 2020-05-19
 */
class SvgWaterTest {
    @Test
    void svgToImage() {
        Properties properties = new Properties();
        properties.setProperty("width", "1700");
        properties.setProperty("height", "2200");
        Water water = new WaterBuilder()
                .from(FileType.SVG).to(FileType.JPG)
                .source("C:/file/1589768338038.svg").target("C:/file")
                .config(properties)
                .build();
        water.convert();
    }

    @Test
    void imageToSvg() {
        Water water = new WaterBuilder()
                .from(FileType.JPG).to(FileType.SVG)
                .source("C:/file/a.jpg").target("C:/file")
                .build();
        water.convert();
    }
}
