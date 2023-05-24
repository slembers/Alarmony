package com.slembers.alarmony.global.redis.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;

import java.time.Duration;

@Service
@RequiredArgsConstructor
public class RedisUtil {
    private final StringRedisTemplate stringRedisTemplate;

    public String getData(String key){

        ValueOperations<String,String> valueOperations = stringRedisTemplate.opsForValue();
        return valueOperations.get(key);
    }

    public void setData(String key, String value){
        ValueOperations<String,String> valueOperations = stringRedisTemplate.opsForValue();
        valueOperations.set(key,value);
    }

      public void setDataExpireWithSecond(String key,String value, long duration){
            ValueOperations<String,String> valueOperations = stringRedisTemplate.opsForValue();
            Duration expireDuration = Duration.ofSeconds(duration);
            valueOperations.set(key,value,expireDuration);
        }

    public void setDataExpireWithDays(String key, String value, long durationInDays) {
        ValueOperations<String, String> valueOperations = stringRedisTemplate.opsForValue();
        Duration expireDuration = Duration.ofDays(durationInDays);
        valueOperations.set(key, value, expireDuration);
    }

    public boolean existToken(String token){
       return stringRedisTemplate.hasKey("Black:"+token);
    }

    public void deleteData(String key){
        stringRedisTemplate.delete(key);
    }
}
