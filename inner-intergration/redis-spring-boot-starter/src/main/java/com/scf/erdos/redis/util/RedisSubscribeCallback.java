package com.scf.erdos.redis.util;

 
public interface RedisSubscribeCallback {
    void callback(String msg);
}
