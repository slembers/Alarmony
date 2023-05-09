package com.slembers.alarmony.member.exception;

import com.slembers.alarmony.global.util.ErrorCode;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;


@AllArgsConstructor
public enum MemberErrorCode implements ErrorCode {

    MEMBER_NOT_FOUND(HttpStatus.NOT_FOUND, "존재하지 않는 멤버입니다."),
    EMAIL_DUPLICATED(HttpStatus.OK, "중복되는 이메일 입니다."),
    EMAIL_NOT_FOUND(HttpStatus.NOT_FOUND,"가입되지 않은 이메일 입니다."),
    ID_DUPLICATED(HttpStatus.OK, "중복되는 아이디 입니다."),

    MEMBER_NOT_ALLOW(HttpStatus.UNAUTHORIZED,"가입 인증이 완료되지 않았습니다. 이메일을 확인해 주세요"),

    PASSWORD_NOT_VALID (HttpStatus.UNPROCESSABLE_ENTITY,"기존 비밀번호를 다시 확인해주세요"),
    INPUT_NOT_VALID(HttpStatus.UNAUTHORIZED, "아이디와 비밀번호를 정확하게 입력해주세요"),
    CHANGE_PASSWORD_NOT_SAME(HttpStatus.UNPROCESSABLE_ENTITY,"변경하고자 하는 비밀번호가 서로 일치하지 않습니다."),


    NICKNAME_DUPLICATED(HttpStatus.OK, "중복되는 닉네임 입니다."),

    MEMBER_REGISTRATION_TOKEN_WRONG(HttpStatus.BAD_REQUEST, "기기 등록 토큰이 잘못되었습니다."),


    AMAZONS3_ERROR(HttpStatus.INTERNAL_SERVER_ERROR,"[Amazaon S3] 이미지 업로드에 실패하였습니다.");


    private final HttpStatus httpStatus;
    private final String detail;

    @Override
    public HttpStatus getHttpStatus() {
        return httpStatus;
    }

    @Override
    public String getDetail() {
        return detail;
    }

    @Override
    public String getName() {
        return name();
    }
}

