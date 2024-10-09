package org.rayala.ps;

import com.google.gson.Gson;

import redis.clients.jedis.Jedis;

public class Publisher<T> {
    public void publish(String channel, T message) {
        Jedis jedis = RedisConnection.getJedis();
        try {
            jedis.publish(channel, new Gson().toJson(message));
        } finally {
            RedisConnection.closeJedis(jedis);
        }
    }
}

