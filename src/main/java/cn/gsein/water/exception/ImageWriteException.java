package cn.gsein.water.exception;

/**
 * @author G. Seinfeld
 * @since 2020-05-19
 */
public class ImageWriteException extends RuntimeException {
    public ImageWriteException(Throwable cause) {
        super("图片输出错误", cause);
    }
}
