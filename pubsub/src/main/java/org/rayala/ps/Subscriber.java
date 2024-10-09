package org.rayala.ps;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.Consumer;

import com.google.gson.Gson;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPubSub;

public class Subscriber<T> extends JedisPubSub {

    private static final ExecutorService threadPool = Executors.newCachedThreadPool();
    private Map<String, List<Consumer<T>>> channelHandlers = new HashMap<>();
    private Class<T> type;
    private Gson gson = new Gson();

    public Subscriber(Class<T> type) {
        this.type = type;
    }
    
    public void onMessage(String channel, String message) {
        channelHandlers.get(channel).parallelStream().forEach(h -> threadPool.execute(() -> {
            h.accept((T) gson.fromJson(message, type));
        }));
    }
    
    public void subscribe(String channel, List<Consumer<T>> handlers) {
        channelHandlers.put(channel, handlers);
        threadPool.submit(() -> {
            Jedis jedis = RedisConnection.getJedis();
            try {
                jedis.subscribe(this, channel);
            } finally {
                RedisConnection.closeJedis(jedis);
            }
        });
    }

    public static Boolean isShutdown() {
        return threadPool.isShutdown();
    }

    public static void shutdown() {
        threadPool.shutdownNow();
    }
}
