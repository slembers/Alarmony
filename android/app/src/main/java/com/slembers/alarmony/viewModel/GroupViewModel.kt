package com.slembers.alarmony.viewModel

import androidx.compose.material3.TimePickerState
import androidx.compose.runtime.mutableStateMapOf
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.map
import com.slembers.alarmony.model.db.dto.MemberDto

class GroupViewModel(
    private val _title : MutableLiveData<String> = MutableLiveData(""),
    private val _alarmTime : MutableLiveData<TimePickerState> = MutableLiveData(TimePickerState(7,0,false)),
    private val _week : MutableLiveData<Map<String,Boolean>> = MutableLiveData(
        mutableStateMapOf(
            "월" to true,
            "화" to true,
            "수" to true,
            "목" to true,
            "금" to true,
            "토" to true,
            "일" to true
        )
    ),
    private val _groupMember : MutableLiveData<List<MemberDto>> = MutableLiveData(listOf()),
    private val _sound : MutableLiveData<String> = MutableLiveData("노래제목"),
    private val _vibration : MutableLiveData<Boolean> = MutableLiveData(true),
    private val _volumn : MutableLiveData<Float> = MutableLiveData(7f)
) : ViewModel() {

    val title : LiveData<String> = _title
    val alarmTime : LiveData<TimePickerState> = _alarmTime
    val week : LiveData<Map<String, Boolean>> = _week
    val groupMember : LiveData<List<MemberDto>> = _groupMember
    val sound : LiveData<String> = _sound
    val vibration : LiveData<Boolean> = _vibration
    val volumn : LiveData<Float> = _volumn

    fun onChangeTitle(title : String) {
        _title.value = title
    }

    fun addAlarmWeeks(week : String) {

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