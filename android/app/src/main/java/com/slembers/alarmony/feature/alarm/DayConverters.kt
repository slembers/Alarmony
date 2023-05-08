package com.slembers.alarmony.feature.alarm

import androidx.room.TypeConverter

class DayConverters { // Room에는 원시타입만 저장이 가능하기 때문에 List를 직렬화해줘야 한다.
    @TypeConverter
    fun fromBooleanList(booleanList: List<Boolean>): String {
        return booleanList.joinToString(separator = ",") { it.toString() }
    }

    @TypeConverter
    fun toBooleanList(booleanListString: String): List<Boolean> {
        return booleanListString.split(",").map { it.toBoolean() }
    }
}