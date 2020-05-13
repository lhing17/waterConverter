package cn.gsein.water;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author G. Seinfeld
 * @since 2020-05-13
 */
class WaterTest {

    @Test
    void name() {
        Water water = new WaterBuilder()
                .from(FileType.PNG)
                .to(FileType.JPG)
                .source("C:/file/a.jpg")
                .target("C:/file/b.png")
                .build();
        System.out.println(water);
        water.convert();
    }
}
