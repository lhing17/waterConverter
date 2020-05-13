The Water Converter is a suite of API written in Java, which aims to convert your file to any format. It was inspired by Convertio, an extension of the Chrome browser.

## Supported Features

## TODO list
-[ ] text -> image
-[ ] image -> image

## Usage
```java
Water water = new WaterBuilder()
        .from(FileType.TEXT)
        .to(FileType.IMAGE)
        .source("C:/file/a.txt")
        .target("C:/file/a.jpg")
        .build();
water.convert();
```
