package com.github.bingoohuang.randomidgenerator;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * Created by bingoohuang on 2017/3/12.
 */
@Data
@Accessors(fluent = true)
public class RandomIdConfig {
    private int expectLen = 10;
    private String noneOf = null;
    private int total = 10_000;
    private String toFile = "random.txt";
    private RandomIdFilter processor = null;
    private boolean onlyDigits;
    private boolean caseSensitive;

    public RandomIdGenerator build() {
        return new RandomIdGenerator(this);
    }
}
