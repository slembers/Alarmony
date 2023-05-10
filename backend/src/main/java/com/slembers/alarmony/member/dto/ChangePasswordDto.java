package com.slembers.alarmony.member.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import javax.validation.constraints.Pattern;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ChangePasswordDto {

    @Pattern(regexp ="^[a-zA-Z\\d]{8,16}$", message = "비밀번호는 영문(대/소문자) 숫자를 포함하여 8자리 이상 16자리 이하여야 합니다.")
    private String oldPassword;
    @Pattern(regexp ="^[a-zA-Z\\d]{8,16}$", message = "비밀번호는 영문(대/소문자) 숫자를 포함하여 8자리 이상 16자리 이하여야 합니다.")
    private String newPassword;
    @Pattern(regexp ="^[a-zA-Z\\d]{8,16}$", message = "비밀번호는 영문(대/소문자) 숫자를 포함하여 8자리 이상 16자리 이하여야 합니다.")
    private String confirmNewPassword;
}
