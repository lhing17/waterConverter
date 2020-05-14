The Water Converter is a suite of convenient API written in Java, which aims to convert your file to **ANY** format. It was inspired by Convertio, an extension of the Chrome browser.

## Supported Features
-[X] text -> image
-[X] image -> image

## TODO list


## Usage
### Do conversion
```java
Water water = new WaterBuilder()
        .from(FileType.TXT)
        .to(FileType.JPG)
        .source("C:/file/a.txt")
        .target("C:/file")
        .build();
water.convert();
```

### Check support conversions
```java
String support = Water.checkSupport();
String toPngSupport = Water.checkSupportByToType(FileType.PNG);
String fromTxtSupport = Water.checkSupportByFromType(FileType.TXT)
```
