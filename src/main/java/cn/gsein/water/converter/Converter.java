package cn.gsein.water.converter;

import cn.gsein.water.FileType;

import java.io.File;
import java.io.InputStream;
import java.util.Properties;

/**
 * 转换器接口
 *
 * @author G. Seinfeld
 * @since 2020-05-13
 */
public interface Converter {

    void convert(FileType from, FileType to, InputStream inputStream, String outputPath);

    void convert(FileType from, FileType to, InputStream inputStream, String outputPath, Properties properties);

    void convert(FileType from, FileType to, File[] inputFiles, String outputPath);

    void convert(FileType from, FileType to, File[] inputFiles, String outputPath, Properties properties);

}
