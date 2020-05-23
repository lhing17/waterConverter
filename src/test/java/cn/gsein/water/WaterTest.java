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
                .from(FileType.PNG)
                .to(FileType.JPG)
                .source("C:/file/0.png")
                .target("C:/file")
                .build();
        water.convert();
    }

    @Test
    void convertTextToImage() {
        Water water = new WaterBuilder()
                .from(FileType.TXT)
                .to(FileType.TIFF)
                .source("C:/file/b.txt")
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
    void convertMultipleTextToPdf() {
        Water water = new WaterBuilder()
                .from(FileType.TXT)
                .to(FileType.PDF)
                .source("C:/file/a.txt", "C:/file/b.txt")
                .target("C:/file")
                .build();
        water.convert();
    }

    @Test
    void convertMultipleImagesToPdf() {
        Water water = new WaterBuilder()
                .from(FileType.PNG)
                .to(FileType.PDF)
                .source("C:/file/0.png", "C:/file/1.png", "C:/file/2.png", "C:/file/3.png")
                .target("C:/file")
                .build();
        water.convert();
    }


    @Test
    void checkSupport() {
        String support = Water.checkAllReadableSupports();
        System.out.println(support);
    }

    @Test
    void checkSupportByFromType() {
        String support = Water.checkReadableSupportsByFromTypes(FileType.PNG);
        System.out.println(support);
    }

    @Test
    void checkSupportByToType() {
        String support = Water.checkReadableSupportsByToTypes(FileType.PNG);
        System.out.println(support);
    }
}
