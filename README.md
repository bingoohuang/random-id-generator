# random-id-generator
可以在27秒可以生成1千万不重复的10位随机数字母数字, 包括写入文件。
<br>
```java
new RandomIdConfig()
        .expectLen(13)
        .total(10_000_000)
        .toFile("random4.txt")
        .build()
        .create();
```