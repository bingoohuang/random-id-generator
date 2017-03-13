# random-id-generator
本机可以在26秒(JAVA实现)/15秒(GO实现)可以生成1千万不重复的10位随机数字母数字, 包括写入文件。
<br>
```java
new RandomIdConfig()
        .expectLen(10)
        .total(10_000_000)
        .toFile("random4.txt")
        .build()
        .create();
```


```
~/g/random-id-generator> go build src/random_id_gen.go
~/g/random-id-generator> ./random_id_gen
14.25488807s
```

```
~/g/random-id-generator> mvn clean test
-------------------------------------------------------
 T E S T S
-------------------------------------------------------
Running com.github.bingoohuang.randomidgenerator.RandomIdGeneratorTest
Tests run: 2, Failures: 0, Errors: 0, Skipped: 1, Time elapsed: 25.641 sec
```

```text
~/g/random-id-generator> head -10 go_random_id.txt
EMXMF23XAU
5ZLDQXU27R
BFNPN24E5P
GISIMIDIFR
ENWZFCXJK7
TFUC43RMEQ
4D2ITQP4YS
7DYYOXDDG6
U3O64NXBIL
FROLDQ42HC
```