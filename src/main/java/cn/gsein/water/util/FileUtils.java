package cn.gsein.water.util;

import java.io.File;
import java.nio.file.Paths;

/**
 * 文件处理工具类
 *
 * @author G. Seinfeld
 * @since 2020-05-15
 */
public final class FileUtils {
    private FileUtils() {
    }

    /**
     * 根据文件路径和文件名，获得完整的文件名
     * <p>
     * 如果文件夹不存在，将创建文件夹
     *
     * @param outputPath 文件路径
     * @param name       文件名（含扩展名）
     * @return 文件完整名称（含路径）
     */
    public static String makeDirAndGetFullName(String outputPath, String name) {
        File file = new File(outputPath);
        if (!file.exists()) {
            //noinspection ResultOfMethodCallIgnored
            file.mkdirs();
        }

        return Paths.get(outputPath, name).toString();
    }
}
