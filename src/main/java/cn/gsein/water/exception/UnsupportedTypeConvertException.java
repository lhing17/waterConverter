package cn.gsein.water.exception;

/**
 * 不支持的类型转换
 *
 * @author G. Seinfeld
 * @since 2020-05-13
 */
public class UnsupportedTypeConvertException extends RuntimeException {

    public UnsupportedTypeConvertException(String message) {
        super(message);
    }

    public UnsupportedTypeConvertException(String message, Throwable cause) {
        super(message, cause);
    }

}
