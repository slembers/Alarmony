package com.slembers.alarmony.global.dto;


import lombok.Getter;

@Getter
public class MessageResponseDto {


    private String code;
    private  String message;

    public MessageResponseDto(String code ,String message) {
        this.code = code;
        this.message = message;
    }
}
