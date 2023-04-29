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
    private val _groupMember : MutableLiveData<MemberDto> = MutableLiveData(null),
    private val _sound : MutableLiveData<String> = MutableLiveData("노래제목"),
    private val _vibration : MutableLiveData<Boolean> = MutableLiveData(true),
    private val _volumn : MutableLiveData<Float> = MutableLiveData(7f)
) : ViewModel() {

    val title : LiveData<String> = _title
    val alarmTime : LiveData<TimePickerState> = _alarmTime
    val week = _week
    val groupMember = ""
    val sound = ""
    val vibration = ""
    val volumn = ""

    fun onChangeTitle(title : String) {
        _title.postValue(title)
    }

    fun onChangeAlarm(alarm : TimePickerState) {
        _alarmTime.value = alarm
    }


}