package com.github.bingoohuang.randomidgenerator;

import org.junit.Test;

/**
 * 27秒可以生成1千万不重复的10位随机数字母数字, 包括写入文件。
 */
public class RandomIdGeneratorTest {
    @Test
    public void test1() {
        new RandomIdConfig().expectLen(10).noneOf("0O=").total(10_000)
                .processor(
                        (randomStr, seqNo) -> "101" + randomStr
                ).toFile("random1.txt").build().create();

        new RandomIdConfig().expectLen(10).onlyDigits(true)
                .total(10_000).toFile("random2.txt")
                .build().create();

        new RandomIdConfig().expectLen(10).caseSensitive(true)
                .total(10_000).toFile("random3.txt")
                .build().create();
    }

    @Test
    public void test2()  {
        new RandomIdConfig().expectLen(13)
                .total(10_000_000).toFile("random4.txt")
                .build().create();
    }
}

