package com.slembers.alarmony.model.db

import androidx.compose.material3.TimePickerState
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.slembers.alarmony.model.db.dto.MemberDto

class GroupModel(
    private val _title : MutableLiveData<String> = MutableLiveData(""),
    private val _alarmTime : MutableLiveData<TimePickerState> = MutableLiveData(TimePickerState(0,0,false)),
    private val _week : MutableLiveData<List<String>> = MutableLiveData(emptyList()),
    private val _groupMember : MutableLiveData<List<MemberDto>> = MutableLiveData(emptyList()),
    private val _sound : MutableLiveData<String> = MutableLiveData("노래제목"),
    private val _vibration : MutableLiveData<Boolean> = MutableLiveData(true),
    private val _volumn : MutableLiveData<Float> = MutableLiveData(7f)
) : ViewModel() {

    val title : LiveData<String> = _title
    val alarmTime : LiveData<TimePickerState> = _alarmTime
    val week : LiveData<List<String>> = _week
    val groupMember : LiveData<List<MemberDto>> = _groupMember
    val sound : LiveData<String> = _sound
    val vibration : LiveData<Boolean> = _vibration
    val volumn : LiveData<Float> = _volumn

    fun onChangeTitle(title : String) {
        _title.value = title
    }

    fun onChangeAlarm(alarm : TimePickerState) {
        _alarmTime.value = alarm
    }

    fun addAlarmWeeks(week : List<String>) {
        _week.value = week
    }

    fun onChangeVibration(vibration : Boolean) {
        _vibration.value = vibration
    }

    fun setAlarmSound(sound : String) {
        _sound.value = sound
    }

    fun onChangeVolumn(volumn : Float) {
        _volumn.value = volumn
    }
}