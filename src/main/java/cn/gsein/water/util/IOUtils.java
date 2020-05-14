package cn.gsein.water.util;

import java.io.Closeable;
import java.io.IOException;

/**
 * @author G. Seinfeld
 * @since 2020-05-14
 */
public final class IOUtils {

    private IOUtils() {
    }

    public static void closeSilently(Closeable... closeables) {
        for (Closeable closeable : closeables) {
            try {
                if (closeable != null) {
                    closeable.close();
                }
            } catch (IOException ignored) {
            }
        }
    }
}
