package com.slembers.alarmony.model.db

import androidx.compose.material3.TimePickerState
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class Group(
    private val _title: MutableLiveData<String> = MutableLiveData(""),
    private val _timestate: MutableLiveData<TimePickerState?> =
        MutableLiveData(TimePickerState(0,0,false)),
    private val _alarm_date : MutableLiveData<List<String>> = MutableLiveData(listOf()),
    private val _members : MutableLiveData<List<String>> = MutableLiveData(listOf()),
    private val _sound_name : MutableLiveData<String> = MutableLiveData("노래제목"),
    private val _sound_volume : MutableLiveData<Float> = MutableLiveData(7f),
    private val _vibrate : MutableLiveData<Boolean> = MutableLiveData(false)
) : ViewModel() {

    var title = _title
    var timestate = _timestate
    var alarm_date = _alarm_date
    var members = _members
    var sound_name = _sound_name
    var sound_volume = _sound_volume
    var vibrate = _vibrate

    fun onChangeTitle(title : String) {
        _title.value = title
    }

    fun onChangeTime(timestate : TimePickerState) {
        _timestate.value = timestate
    }

    fun onChangeMusic(soundName : String) {
        _sound_name.value = soundName
    }

    fun onChangeVolume(soundVolume : Float) {
        _sound_volume.value = soundVolume
    }


}