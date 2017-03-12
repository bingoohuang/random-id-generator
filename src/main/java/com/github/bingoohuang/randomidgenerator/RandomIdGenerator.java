package com.github.bingoohuang.randomidgenerator;

import com.google.common.base.CharMatcher;
import com.google.common.base.Charsets;
import com.google.common.hash.BloomFilter;
import com.google.common.hash.HashFunction;
import com.google.common.hash.Hashing;
import com.google.common.io.BaseEncoding;
import com.google.common.io.Closer;
import com.google.common.primitives.UnsignedLongs;
import lombok.SneakyThrows;
import lombok.val;

import java.io.BufferedOutputStream;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.security.SecureRandom;

import static com.google.common.base.CharMatcher.javaLetterOrDigit;
import static com.google.common.base.CharMatcher.noneOf;
import static com.google.common.io.BaseEncoding.base32;
import static com.google.common.io.BaseEncoding.base64;

/**
 * 随机字符串生成。
 * 使用bloom filter保证唯一性.
 */
public class RandomIdGenerator {
    private final RandomIdConfig randomIdConfig;
    private final BloomFilter<String> bloomFilter;
    // base32: A-Z 2-7 Human-readable; no possibility of mixing up 0/O or 1/I. Defaults to upper case.
    private final BaseEncoding baseEncoding;
    private final CharMatcher containsCharMatcher;  // 保留字符匹配器
    private SecureRandom random = new SecureRandom();
    private HashFunction hf = Hashing.murmur3_128();
    private Closer closer = Closer.create();
    private PrintStream ps;

    /**
     * 构造函数，配合生成器使用。
     *
     * @param randomIdConfig 随即字符串生成器
     */
    RandomIdGenerator(RandomIdConfig randomIdConfig) {
        this.randomIdConfig = randomIdConfig;

        val noneOf = randomIdConfig.noneOf();
        containsCharMatcher = noneOf == null ? javaLetterOrDigit() : javaLetterOrDigit().and(noneOf(noneOf));

        bloomFilter = BloomFilter.create(
                (str, primitiveSink) -> primitiveSink.putString(str, Charsets.UTF_8),
                randomIdConfig.total() * 2
        );
        baseEncoding = randomIdConfig.caseSensitive() ? base64() : base32();

        createOut();
    }

    /*
     * 创建缓冲文件输出。
     */
    @SneakyThrows
    private void createOut() {
        val fos = closer.register(new FileOutputStream(randomIdConfig.toFile()));
        val bos = closer.register(new BufferedOutputStream(fos));
        ps = closer.register(new PrintStream(bos));
    }

    /**
     * 生成随机字符串文件。
     *
     * @return 生成文件所耗毫秒数。
     */
    @SneakyThrows
    public long create() {
        long startTime = System.currentTimeMillis();
        val processor = randomIdConfig.processor();
        for (int i = 0, ii = randomIdConfig.total(); i < ii; ++i) {
            String single = createSingle();
            if (processor != null) single = processor.filter(single, i + 1);
            ps.println(single);
        }

        closer.close();

        return System.currentTimeMillis() - startTime;
    }

    /**
     * 生成单个随机字符串。
     *
     * @return 随机字符串
     */
    public String createSingle() {
        val id = adjustLen(createId(), randomIdConfig.expectLen());

        if (!bloomFilter.mightContain(id)) {
            bloomFilter.put(id);
            return id;
        }

        return createSingle();
    }

    private String createId() {
        long randLong = random.nextLong();
        if (randomIdConfig.onlyDigits()) {
            return UnsignedLongs.toString(randLong);
        }

        byte[] hashBytes = hf.hashLong(randLong).asBytes();
        String hash = baseEncoding.encode(hashBytes);  // 32位
        return containsCharMatcher.retainFrom(hash);
    }

    // 调整字符串到固定长度。如果超长则尾部截断，否则尾部补充0123456789.
    private String adjustLen(String value, int len) {
        int valueLen = value.length();
        if (valueLen >= len) return value.substring(0, len);

        return value + "0123456789".substring(len - valueLen);
    }
}
