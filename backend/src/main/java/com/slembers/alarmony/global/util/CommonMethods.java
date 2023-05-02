package com.slembers.alarmony.global.util;

import com.slembers.alarmony.alarm.exception.AlarmErrorCode;
import com.slembers.alarmony.global.execption.CustomException;

import java.util.ArrayList;
import java.util.List;

public class CommonMethods {
    static String[] weekdays = new String[]{"월", "화", "수", "목", "금", "토", "일"};

    /**
     * 알람 정보에서, String 졍보를 boolean 리스트로 변환한다.
     * DB 컬럼 -> AOS 전송용으로 변경
     *
     * @param dates 요일 정보
     * @return 요일별 boolean 값
     */
    public static List<Boolean> changeStringToBooleanList(String dates) {
        List<Boolean> targetWeekdays = new ArrayList<>();
        if(dates.length() != 7) throw new CustomException(AlarmErrorCode.ALARM_DATE_INFO_WRONG);
        for (int i = 0; i < 7; i++) {
            if (dates.charAt(i) == '1') {
                targetWeekdays.add(true);
            }
            else {
                targetWeekdays.add(false);
            }
        }
        return targetWeekdays;
    }

    /**
     * 알람 정보에서, boolean 리스트를 string 요일 정보로 변환한다.
     * AOS 요청 정보 -> DB 저장용 정보로 변환
     *
     * @param dates
     * @return
     */
    public static String changeBooleanListToString(List<Boolean> dates) {
        StringBuilder targetWeekdays = new StringBuilder();

        if(dates == null || dates.size() != 7) throw new CustomException(AlarmErrorCode.ALARM_DATE_INFO_WRONG);
        for (int i = 0; i < 7; i++) {
            if (dates.get(i)) targetWeekdays.append("1");
            else targetWeekdays.append("0");
        }
        return targetWeekdays.toString();
    }
}
