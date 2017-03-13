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

## GO测试约15秒，JAVA跑测试，约26秒
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

## 示例生成的数据文件
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

## DOCKER准备ORACLE测试环境
```
docker run --name oracle-xe -d -v /Users/bingoohuang/docker-share:/var/docker-share -p 49160:22 -p 49161:1521 -e ORACLE_ALLOW_REMOTE=true wnameless/oracle-xe-11g

docker exec -it oracle-xe  bash

Connect database with following setting:
hostname: localhost
port: 49161
sid: xe
username: system
password: oracle
Password for SYS & SYSTEM:oracle

-- 创建表
create table random_str ( str char(10));
-- 有索引的情况下，先行不用索引
drop index IDX;
-- 数据倒入完成后，再添加索引
create unique index idx on RANDOM_STR (str);
```

## 控制文件 radom_str.ctl
```
LOAD DATA
INFILE "/var/docker-share/go_random_id.txt"
APPEND INTO TABLE random_str
FIELDS TERMINATED BY ','
(str)
```

## 导入ORACLE
```bash
START=$(date +%s.%N)
# 注意，direct=true速度非常快，建议开启
sqlldr system/oracle control=/var/docker-share/random_id.ctl log=random_id.log parallel=true direct=true
END=$(date +%s.%N)
DIFF=$(echo "$END - $START" | bc)
echo $DIFF
```
