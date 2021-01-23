package lxy;

import lombok.extern.slf4j.Slf4j;
import org.openjdk.jmh.annotations.Setup;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;
import org.redisson.Redisson;
import org.redisson.api.LocalCachedMapOptions;
import org.redisson.api.RLocalCachedMap;
import org.redisson.api.RMapCache;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.redisson.config.TransportMode;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.LockSupport;

@Slf4j
public class TestMain {

    public static void main(String[] args) {

        RedissonClient redissonClient;
        RMapCache<RedisKey, RedisValue> redisMap;
        RLocalCachedMap<RedisKey, RedisValue> redisCacheMap;
//            Config config = new Config();
//            config.setTransportMode(TransportMode.NIO);

            redissonClient = Redisson.create();
        // RMapCache<RedisKey, RedisValue> options = LocalCachedMapOptions.defaults();
            redisMap = redissonClient.getMapCache("rmapCache");
        redisCacheMap = redissonClient.getLocalCachedMap("rlocalcachemap", LocalCachedMapOptions.defaults());

        RedisKey redisKey = RedisKey.builder().a("aa").b("bbb").build();
        RedisValue redisValue = RedisValue.builder().c("ccc").d("dddd").build();

        redisCacheMap.put(redisKey, redisValue);

        redisCacheMap.get(redisKey);
        redisMap.put(redisKey, redisValue, 3, TimeUnit.SECONDS);
        var tmp = redisMap.get(redisKey);
        log.error("lixiangyang debug. first redis value: {}", tmp);
            LockSupport.parkNanos((long)5e9);
            tmp = redisMap.get(redisKey);
           log.error("lixiangyang debug. after sleep 5s ,redis value: {}", tmp);
        redissonClient.shutdown();
    }


}
