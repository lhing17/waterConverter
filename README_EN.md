The Water Converter is a suite of convenient API written in Java, which aims to convert your file to **ANY** format by ONE LINE code. It was inspired by Convertio, an extension of the Chrome browser.

## Supported Features

### supported conversions

-[X] text -> image
-[X] image -> image
-[X] text -> pdf
-[X] image -> pdf

### supported image type

- png
- jpg/jpeg
- bmp
- tiff
- gif

## TODO list

- excel support
- html support


This project is under active development, we might change APIs without notice.

## Usage
### Do conversion
```java
// convert a single text file into jpeg file
Water water = new WaterBuilder()
        .from(FileType.TXT)
        .to(FileType.JPG)
        .source("C:/file/a.txt")
        .target("C:/file")
        .build();
water.convert();
```

```java
// convert multiple png files into one pdf file
Water water = new WaterBuilder()
        .from(FileType.PNG)
        .to(FileType.PDF)
        .source("C:/file/0.png", "C:/file/1.png", "C:/file/2.png", "C:/file/3.png")
        .target("C:/file")
        .build();
water.convert();
```

### Check support conversions
```java
// check all support conversions
String support = Water.checkSupport();

// check support conversions to specified type
String toPngSupport = Water.checkSupportByToType(FileType.PNG);

// check support conversions from specified type
String fromTxtSupport = Water.checkSupportByFromType(FileType.TXT);
```
