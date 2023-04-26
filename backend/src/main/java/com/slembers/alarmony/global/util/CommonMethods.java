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

    /**
     * 신규 알람을 생성할 때, 요일 리스트를 DB 저장용 바이트 배열로 변환한다.
     * @param list 요일 리스트
     * @return 바이트 배열
     */
    public static Byte[] changeStringListToByteList(List<String> list) {
        Byte[] bytes = new Byte[7];

        for (int i = 0; i < 7; i++) {
            bytes[i] = 0;
            for (int j = 0, size = list.size(); j < size; j++) {
                if (list.get(j).equals(weekdays[j])) {
                    bytes[i] = 1;
                    break;
                }
            }
        }
        return bytes;
    }
}
