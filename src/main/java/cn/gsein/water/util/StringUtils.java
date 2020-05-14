package cn.gsein.water.util;

import java.util.ArrayList;
import java.util.List;

/**
 * 字符串工具类
 *
 * @author G. Seinfeld
 * @since 2020-05-14
 */
public final class StringUtils {
    private StringUtils() {
    }

    /**
     * 将字符串按长度切成片段
     *
     * @param source      源字符串
     * @param sliceLength 每个片段的长度
     * @return 切成片段的字符串列表
     */
    public static List<String> slice(String source, int sliceLength) {
        List<String> list = new ArrayList<>();
        // 分切片数
        int count = (source.length() - 1) / sliceLength + 1;
        for (int i = 0; i < count; i++) {
            list.add(source.substring(i * sliceLength, Math.min(source.length(), (i + 1) * sliceLength)));
        }
        return list;
    }

}
