package com.slembers.alarmony.member.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;

@Getter
@Setter
@AllArgsConstructor
public class SignUpDto {

    @NotEmpty(message = "아이디는 필수 입력값입니다.")
    @Pattern(regexp = "^[a-z0-9]{5,11}$", message = "아이디는 영어 소문자와 숫자만 사용하여 4자리 이상 20자리 이하여야 합니다.")
    private String username;

    @Pattern(regexp ="^[a-zA-Z\\d]{8,16}$", message = "비밀번호는 영문(대/소문자) 숫자를 포함하여 8자리 이상 16자리 이하여야 합니다.")
    private String password;

    @NotEmpty(message = "닉네임은 필수 입력값입니다.")
    @Pattern(regexp = "^[가-힣a-zA-Z0-9]{2,10}$" , message = "닉네임은 특수문자를 포함하지 않은 2자리 이상 10자리 이하여야 합니다.")
    private String nickname;

    @NotEmpty(message = "이메일은 필수 입력값입니다.")
    @Email
    private String email;
}
