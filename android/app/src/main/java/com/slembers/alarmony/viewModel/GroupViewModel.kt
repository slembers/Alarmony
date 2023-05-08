@file:OptIn(ExperimentalMaterial3Api::class)

package com.slembers.alarmony.viewModel

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.TimePickerState
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateMapOf
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.slembers.alarmony.model.db.dto.MemberDto
import javax.inject.Inject

@ExperimentalMaterial3Api
class GroupViewModel : ViewModel() {

    private val _title = MutableLiveData("")
    private val _alarmTime = MutableLiveData(
        TimePickerState(10,0,false))
    private val _alarmWeeks = mutableStateMapOf<String, Boolean>()
    private val _currentWeeks = MutableLiveData<MutableMap<String,Boolean>>()
    private val _members = mutableStateListOf<MemberDto>()
    private val _currentMembers = MutableLiveData<MutableList<MemberDto>>()
    private val _sound = MutableLiveData("노래제목")
    private val _vibrate = MutableLiveData(true)
    private val _volumn = MutableLiveData(7f)

    init {
        val map = mapOf<String,Boolean>(
            Pair(first = "월", second = true),
            Pair(first = "화", second = true),
            Pair(first = "수", second = true),
            Pair(first = "목", second = true),
            Pair(first = "금", second = true),
            Pair(first = "토", second = true),
            Pair(first = "일", second = true),
        )
        _alarmWeeks.putAll(map)
        _currentWeeks.value = _alarmWeeks
        _currentMembers.value = mutableListOf()
    }

    val title : LiveData<String> = _title
    val alarmTime : LiveData<TimePickerState> = _alarmTime
    val currentWeeks : LiveData<MutableMap<String,Boolean>> = _currentWeeks
    val members : LiveData<MutableList<MemberDto>> = _currentMembers
    val sound : LiveData<String> = _sound
    val vibrate : LiveData<Boolean> = _vibrate
    val volumn : LiveData<Float> = _volumn

    fun onChangeTitle(title : String) {
        _title.postValue(title)
    }

    fun addMember(member : MemberDto) {
        _members.add(member)
        _currentMembers.postValue(_members)
    }

    fun removeMember(member : MemberDto) {
        _members.remove(member)
        _currentMembers.postValue(_members)
    }

    fun onChangeWeek(key : String, value : Boolean) {
        _alarmWeeks[key] = value
        _currentWeeks.value = _alarmWeeks
    }

    fun getIsWeek(key : String) : Boolean {
        return _currentWeeks.value!!.getValue(key)
    }

    fun onChangeSound(sound : String) {
        _sound.postValue(sound)
    }

    fun onChangeVibrate(vibrate : Boolean) {
        _vibrate.postValue(vibrate)
    }

    fun onChangeVolume(volume : Float) {
        _volumn.postValue(volume)
    }

}