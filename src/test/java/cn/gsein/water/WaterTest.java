package cn.gsein.water;

import org.junit.jupiter.api.Test;

/**
 * @author G. Seinfeld
 * @since 2020-05-13
 */
class WaterTest {

    @Test
    void convertImageToImage() {
        Water water = new WaterBuilder()
                .from(FileType.GIF)
                .to(FileType.TIFF)
                .source("C:/file/0.gif")
                .target("C:/file")
                .build();
        water.convert();
    }

    @Test
    void convertTextToImage() {
        Water water = new WaterBuilder()
                .from(FileType.TXT)
                .to(FileType.TIFF)
                .source("C:/file/a.txt")
                .target("C:/file")
                .build();
        water.convert();
    }

    @Test
    void convertTextToPdf() {
        Water water = new WaterBuilder()
                .from(FileType.TXT)
                .to(FileType.PDF)
                .source("C:/file/a.txt")
                .target("C:/file")
                .build();
        water.convert();
    }

    @Test
    void checkSupport() {
        String support = Water.checkSupport();
        System.out.println(support);
    }

    @Test
    void checkSupportByFromType() {
        String support = Water.checkSupportByFromType(FileType.PNG);
        System.out.println(support);
    }

    @Test
    void checkSupportByToType() {
        String support = Water.checkSupportByToType(FileType.PNG);
        System.out.println(support);
    }
}
