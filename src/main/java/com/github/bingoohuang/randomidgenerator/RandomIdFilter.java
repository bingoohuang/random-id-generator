package com.github.bingoohuang.randomidgenerator;

/**
 * 随机数处理器.
 * 在生成随机数后，回掉该接口，以添加额外字段，或者添加前缀/后缀等其他处理。
 * User: Bingoo
 * Date: 13-4-29
 */
public interface RandomIdFilter {
    /**
     * 随机数处理调用。
     * @param randomStr 随机数
     * @param seqNo 生成序号（从1开始）
     * @return 处理后的随机数
     */
    String filter(String randomStr, int seqNo);
}
