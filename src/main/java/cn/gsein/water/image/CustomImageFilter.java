package cn.gsein.water.image;

import java.awt.image.RGBImageFilter;

/**
 * 图片颜色过滤器
 *
 * @author G. Seinfeld
 * @since 2020-05-15
 */
public class CustomImageFilter extends RGBImageFilter {
    @Override
    public int filterRGB(int x, int y, int rgb) {
        // 将白色转为透明
        if (0xffffff == (rgb & 0xffffff)) {
            return 0;
        }
        return rgb;
    }
}
