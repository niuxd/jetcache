package com.alicp.jetcache.redis;

import com.alicp.jetcache.test.AbstractCacheTest;
import com.alicp.jetcache.support.FastjsonKeyConvertor;
import com.alicp.jetcache.support.KryoValueDecoder;
import com.alicp.jetcache.support.KryoValueEncoder;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import org.junit.Test;
import redis.clients.jedis.JedisPool;

import java.util.Random;
import java.util.concurrent.TimeUnit;

/**
 * Created on 2016/10/8.
 *
 * @author <a href="mailto:yeli.hl@taobao.com">huangli</a>
 */
public class RedisCacheTest extends AbstractCacheTest {

    @Test
    public void test() throws Exception {
        GenericObjectPoolConfig pc = new GenericObjectPoolConfig();
        pc.setMinIdle(2);
        pc.setMaxIdle(10);
        pc.setMaxTotal(10);
        JedisPool pool = new JedisPool(pc, "localhost", 6379);

        cache = RedisCacheBuilder.createRedisCacheBuilder()
                .keyConvertor(FastjsonKeyConvertor.INSTANCE)
                .valueEncoder(KryoValueEncoder.INSTANCE)
                .valueDecoder(KryoValueDecoder.INSTANCE)
                .jedisPool(pool)
                .keyPrefix(new Random().nextInt() + "")
                .expireAfterWrite(200, TimeUnit.MILLISECONDS)
                .buildCache();
        baseTest();
        expireAfterWriteTest(cache.config().getDefaultExpireInMillis());

        cache = RedisCacheBuilder.createRedisCacheBuilder()
                .keyConvertor(FastjsonKeyConvertor.INSTANCE)
                .valueEncoder(KryoValueEncoder.INSTANCE)
                .valueDecoder(KryoValueDecoder.INSTANCE)
                .jedisPool(pool)
                .keyPrefix(new Random().nextInt() + "")
                .expireAfterAccess(200, TimeUnit.MILLISECONDS)
                .buildCache();
        baseTest();
        expireAfterAccessTest(cache.config().getDefaultExpireInMillis());

        cache = RedisCacheBuilder.createRedisCacheBuilder()
                .keyConvertor(FastjsonKeyConvertor.INSTANCE)
                .valueEncoder(KryoValueEncoder.INSTANCE)
                .valueDecoder(KryoValueDecoder.INSTANCE)
                .jedisPool(pool)
                .keyPrefix(new Random().nextInt() + "")
                .buildCache();
        keyCoverterTest();

        int thread = 10;
        int count = 100;
        int time = 1000;
        cache = RedisCacheBuilder.createRedisCacheBuilder()
                .keyConvertor(FastjsonKeyConvertor.INSTANCE)
                .valueEncoder(KryoValueEncoder.INSTANCE)
                .valueDecoder(KryoValueDecoder.INSTANCE)
                .jedisPool(pool)
                .keyPrefix(new Random().nextInt() + "")
                .buildCache();
        concurrentTest(thread, count, time);
    }
}
