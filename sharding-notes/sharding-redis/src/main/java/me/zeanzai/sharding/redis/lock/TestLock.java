package me.zeanzai.sharding.redis.lock;

import org.redisson.Redisson;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;

/**
 * @author shawnwang
 */
public class TestLock {

    public static void main(String[] args) throws InterruptedException {
        Config config = new Config();
        config.useSingleServer().setAddress("redis://192.168.1.150:6379");
        RedissonClient redissonClient = Redisson.create(config);

        RLock myDistributeLock = redissonClient.getLock("myDistributeLock");

        myDistributeLock.lock();
        myDistributeLock.lock();
        myDistributeLock.lock();
        myDistributeLock.lock();
        myDistributeLock.lock();
        myDistributeLock.lock();
        myDistributeLock.lock();
        Thread.sleep(1000 *30L);
        myDistributeLock.unlock();
        Thread.sleep(1000 *30L);
        myDistributeLock.unlock();
        Thread.sleep(1000 *30L);
        myDistributeLock.unlock();
        Thread.sleep(1000 *30L);
        myDistributeLock.unlock();
        Thread.sleep(1000 *30L);
        myDistributeLock.unlock();
        Thread.sleep(1000 *30L);
        myDistributeLock.unlock();
        Thread.sleep(1000 *30L);
        myDistributeLock.unlock();

    }
}
