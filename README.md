Water转换器是一套基于Java编写的API接口，旨在通过一行代码将你的文件转成任意格式。此项目受到了Chrome插件Convertio的启发。


## 当前支持的特性

### 当前支持以下转换

-[X] text -> image
-[X] image -> image
-[X] text -> pdf
-[X] image -> pdf

### 当前支持以下图片格式

- png
- jpg/jpeg
- bmp
- tiff
- gif
- svg

## TODO清单

- 支持excel
- 支持html

由于本项目还处于活跃开发的状态，我们可能直接更改API而不进行警告。

## 如何使用
### 进行格式转换
```java
// 将文本文件转为JPG格式
Water water = new WaterBuilder()
        .from(FileType.TXT)
        .to(FileType.JPG)
        .source("C:/file/a.txt")
        .target("C:/file")
        .build();
water.convert();
```

```java
// 将多张图片转为PDF
Water water = new WaterBuilder()
        .from(FileType.PNG)
        .to(FileType.PDF)
        .source("C:/file/0.png", "C:/file/1.png", "C:/file/2.png", "C:/file/3.png")
        .target("C:/file")
        .build();
water.convert();
```

### 查看当前支持哪些转换
```java
// 查看所有支持的转换
String support = Water.checkAllReadableSupports();

// 查看可以从哪些类型转换为指定类型
String toPngSupport = Water.checkReadableSupportsByFromTypes(FileType.PNG);

// 查看从指定类型可以转换为哪些类型
String fromTxtSupport = Water.checkReadableSupportsByToTypes(FileType.TXT);
```
