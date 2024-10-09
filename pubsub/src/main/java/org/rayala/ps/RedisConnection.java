package org.rayala.ps;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

public class RedisConnection {
    private static JedisPool jedisPool = null;

    private RedisConnection() {}

    public static Jedis getJedis() {
        if (jedisPool == null) {
            synchronized (RedisConnection.class) {
                if (jedisPool == null) {
                    jedisPool = new JedisPool("localhost", 6379);
                }
            }
        }
        return jedisPool.getResource();
    }

    public static void closeJedis(Jedis jedis) {
        if (jedis != null) {
            jedis.close();
        }
    }
}

