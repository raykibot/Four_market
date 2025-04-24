package com.luo.infrastructure.redis;

import org.redisson.api.RBlockingDeque;
import org.redisson.api.RBlockingQueue;
import org.redisson.api.RDelayedQueue;

import java.util.concurrent.TimeUnit;

public interface IRedisService {


    /**
     * 设置值  redisson框架，原子性设置，避免导致并发问题
     *
     * @param key
     * @param value
     */
    void setActomicLongValue(String key, long value);


    /**
     * 设置值 直接存储
     *
     * @param key
     * @param value
     * @param <T>
     */
    <T> void setValue(String key, T value);


    /**
     * 获取值
     *
     * @param key
     * @param <T>
     * @return
     */
    <T> T getValue(String key);


    /**
     * 减少值
     *
     * @param key
     * @return
     */
    long decr(String key);


    /**
     * 设置值，如果存在则不设置
     *
     * @param key
     * @return
     */
    Boolean setNx(String key);

    Boolean setNx(String key, long expire, TimeUnit timeUnit);


    /**
     * 获取加锁队列
     * @param key
     * @return
     * @param <T>
     */
    <T> RBlockingQueue<T> getBlockingDeque(String key);


    /**
     * 获取延迟队列
     * @param blockingQueue
     * @return
     * @param <T>
     */
    <T> RDelayedQueue<T> getDelayedQueue( RBlockingQueue<T> blockingQueue);


}
