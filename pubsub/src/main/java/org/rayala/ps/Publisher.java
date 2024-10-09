package org.rayala.ps;

import com.google.gson.Gson;

import lombok.extern.java.Log;
import redis.clients.jedis.Jedis;

@Log
public class Publisher<T> {
    public void publish(String channel, T message) {
        Jedis jedis = RedisConnection.getJedis();
        try {
            log.info(String.format("publish message to channel: %s", channel));
            jedis.publish(channel, new Gson().toJson(message));
        } finally {
            RedisConnection.closeJedis(jedis);
        }
    }
}

