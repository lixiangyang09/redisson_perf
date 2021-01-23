package lxy;

/**
 * 比较Redisson的localMap和java的map的get效率
 *
 */

import org.openjdk.jmh.annotations.*;
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

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

@BenchmarkMode(Mode.Throughput)
@Warmup(iterations = 0)
@Measurement(iterations = 4, time = 5, timeUnit = TimeUnit.SECONDS)
@Threads(1)
@OutputTimeUnit(TimeUnit.MILLISECONDS)
@Fork(1)
@State(Scope.Benchmark)
public class RedissonPerfTest {

    RedissonClient redissonClient;
    RLocalCachedMap<RedisKey, RedisValue> redisLocalCacheMap;
    RMapCache<RedisKey, RedisValue> redisCacheMap;

    Map<RedisKey, RedisValue> localMap;

    RedisKey existKey;
    RedisKey nonExistKey;

    @Setup
    public void prepare() {
//        Config config = new Config();
//        config.setTransportMode(TransportMode.NIO);

        redissonClient = Redisson.create();
        redisLocalCacheMap = redissonClient.getLocalCachedMap("rLocalCacheMap", LocalCachedMapOptions.defaults());
        redisCacheMap = redissonClient.getMapCache("rMapCache");

        localMap = new ConcurrentHashMap<>();

        existKey = RedisKey.builder().a("aa").b("bb").build();

        RedisValue value = RedisValue.builder().c("ccc").d("dddd").build();

        redisLocalCacheMap.put(existKey, value);
        localMap.put(existKey, value);
        redisCacheMap.put(existKey, value);

        nonExistKey = RedisKey.builder().a("not").b("not").build();


    }

    @TearDown
    public void shutdown() {
        redissonClient.shutdown();
    }


    @Benchmark
    public void testMapExistGet() {
        localMap.get(existKey);
    }

    @Benchmark
    public void testMapMissGet() {
        localMap.get(nonExistKey);
    }

    @Benchmark
    public void testRedissonLocalCacheMapExistGet() {
        redisLocalCacheMap.get(existKey);
    }

    @Benchmark
    public void testRedissonLocalCacheMapMissGet() {
        redisLocalCacheMap.get(nonExistKey);
    }

    @Benchmark
    public void testRedissonCacheMapExistGet() {
        redisCacheMap.get(existKey);
    }

    @Benchmark
    public void testRedissonCacheMapMissGet() {
        redisCacheMap.get(nonExistKey);
    }


    public static void main(String[] args) throws RunnerException {
        Options options = new OptionsBuilder()
                .include(RedissonPerfTest.class.getSimpleName())
                .build();
        new Runner(options).run();
    }
}