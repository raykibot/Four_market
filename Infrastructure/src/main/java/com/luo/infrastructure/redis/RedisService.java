package com.luo.infrastructure.redis;





import org.redisson.api.RBucket;
import org.redisson.api.RedissonClient;

import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class RedisService implements IRedisService {

    @Resource
    private RedissonClient redissonClient;


    @Override
    public void setActomicLongValue(String key, long value) {
        redissonClient.getAtomicLong(key).set(value);
    }

    @Override
    public <T> void setValue(String key, T value) {
        redissonClient.getBucket(key).set(value);
    }

    @Override
    public <T> T getValue(String key) {
        RBucket<T> bucket = redissonClient.getBucket(key);
        return bucket.get();
    }

    @Override
    public long decr(String key) {
        return redissonClient.getAtomicLong(key).decrementAndGet();
    }

    @Override
    public Boolean setNx(String key) {
        return redissonClient.getBucket(key).trySet("lock");
    }
}
