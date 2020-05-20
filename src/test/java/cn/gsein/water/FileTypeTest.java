package cn.gsein.water;

import org.junit.jupiter.api.Test;

import java.io.IOException;

/**
 * @author G. Seinfeld
 * @since 2020-05-20
 */
class FileTypeTest {

    @Test
    void getFileTypeByName() throws IOException {

        for (int i = 0; i < 1000; i++) {
            new Thread(() -> System.out.println(FileType.getFileTypeByName("pdf"))).start();
        }

        System.in.read();
    }
}
