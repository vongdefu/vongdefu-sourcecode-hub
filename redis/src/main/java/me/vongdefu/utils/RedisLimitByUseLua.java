package me.vongdefu.utils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.data.redis.core.script.RedisScript;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author shawnwang
 * @version 1.0
 * @describe
 * @date 2023/4/23
 */
@Component
public class RedisLimitByUseLua {

    @Autowired
    private static RedisTemplate redisTemplate;

    @Autowired
    public void setRedisTemplate(RedisTemplate redisTemplate) {
        RedisLimitByUseLua.redisTemplate = redisTemplate;
    }

    public static boolean accquire() throws IOException, URISyntaxException {

        List<String> keys = new ArrayList<>();
        List<String> args = new ArrayList<>();

        String lua =
                "local key = KEYS[1] " +
                        " local limit = tonumber(ARGV[1]) " +
                        " local current = tonumber(redis.call('get', key) or '1')" +
                        " if current + 1 > limit " +
                        " then  return 0 " +
                        " else " +
                        " redis.call('INCRBY', key,'1')" +
                        " redis.call('expire', key,'20') " +
                        " end return 1 ";


        String key = "ip:" + System.currentTimeMillis() / 1000; // 当前秒
        String limit = "3"; // 最大限制

        keys.add(key);
        args.add(limit);

        RedisScript redisScript = new DefaultRedisScript(lua , Long.class);
        Object result = redisTemplate.execute(redisScript, keys, args);
        return ((Long)result) ==1;
    }
}
