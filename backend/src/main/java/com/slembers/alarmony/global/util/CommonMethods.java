package com.slembers.alarmony.global.util;

import java.util.ArrayList;
import java.util.List;

public class CommonMethods {
    static String[] weekdays = new String[]{"월", "화", "수", "목", "금", "토", "일"};

    /**
     * 알람 정보에서, Byte 배열로 된 알람 요일 정보를 스트링 배열로 변환하여 반환한다.
     *
     * @param bytes
     * @return
     */
    public static List<String> changeByteListToStringList(Byte[] bytes) {
        List<String> targetWeekdays = new ArrayList<>();

        for (int i = 0, size = bytes.length; i < size; i++) {
            if (bytes[i] == 1) targetWeekdays.add(weekdays[i]);
        }

        return targetWeekdays;
    }
}
