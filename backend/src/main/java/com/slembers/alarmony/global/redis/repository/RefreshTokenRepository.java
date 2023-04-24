package com.slembers.alarmony.global.redis.repository;

import com.slembers.alarmony.global.jwt.RefreshToken;
import org.springframework.data.repository.CrudRepository;


//CurdRepository 를 상속하고 첫번째 제네릭 타입에는 데이터를 저장할 객체의 클래스를, 두번째로는 객체의 ID 값 (@Id 어노테이션이 붙은) 타입 클래스를 넣어준다.

public interface RefreshTokenRepository extends CrudRepository<RefreshToken, String> {
}
