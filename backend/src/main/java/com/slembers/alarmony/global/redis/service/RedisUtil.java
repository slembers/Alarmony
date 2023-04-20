package com.slembers.alarmony.global.redis.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;

import java.time.Duration;

@Service
@RequiredArgsConstructor
public class RedisUtil {


    /**
     * StringRedisTemplate는 Redis와 상호 작용하기 위한 Spring Data Redis의 구성요소 중 하나입니다
     * StringRedisTemplate을 사용하여 Redis 데이터베이스와 상호 작용합니다.
     */
    private final StringRedisTemplate stringRedisTemplate;


    /**
     * Redis에서 특정 키에 해당하는 값을 반환하는 메소드입니다.
     * @param key (key ,value) 에 "key"에 해당합니다.
     * @return Redis에서 key에 해당하는 value를 리턴합니다.
     */
    public String getData(String key){
        ValueOperations<String,String> valueOperations = stringRedisTemplate.opsForValue();
        return valueOperations.get(key);
    }


    /**
     * Redis에 특정 키-값 쌍을 저장하는 메소드입니다.
     * @param key (key ,value) 에 "key"에 해당합니다.
     * @param value (key ,value) 에 "value"에 해당합니다.
     */
    // Redis에 특정 키-값 쌍을 저장하는 메소드입니다.
    public void setData(String key, String value){
        ValueOperations<String,String> valueOperations = stringRedisTemplate.opsForValue();
        valueOperations.set(key,value);
    }

    /**
     * Redis에 특정 키-값 쌍을 저장하면서, 지정된 기간 후 만료되도록 하는 메소드입니다.
     * @param key (key ,value) 에 "key"에 해당합니다.
     * @param value (key ,value) 에 "value"에 해당합니다.
     * @param duration 초단위 기준으로 입력합니다. 예를들어 300이라면 5분을 의미합니다.
     */
    public void setDataExpire(String key,String value,long duration){
        ValueOperations<String,String> valueOperations = stringRedisTemplate.opsForValue();
        Duration expireDuration = Duration.ofSeconds(duration);
        valueOperations.set(key,value,expireDuration);
    }

    /**
     * Redis에서 특정 키-값 쌍을 삭제하는 메소드입니다.
     * @param key 삭제하고자하는 key 값을 입력합니다.
     */
    public void deleteData(String key){
        stringRedisTemplate.delete(key);
    }
}
