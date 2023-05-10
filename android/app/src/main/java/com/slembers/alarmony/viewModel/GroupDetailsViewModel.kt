package com.slembers.alarmony.viewModel

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.slembers.alarmony.feature.alarm.Alarm
import com.slembers.alarmony.feature.alarm.AlarmDatabase
import com.slembers.alarmony.feature.alarm.AlarmDto
import com.slembers.alarmony.feature.alarm.AlarmRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class GroupDetailsViewModel(val application: Application) : ViewModel() {
    lateinit var repository: AlarmRepository
    private val _Alarm = MutableLiveData<Alarm?>()

    init {
        val alarmDao = AlarmDatabase.getInstance(application).alarmDao()
        CoroutineScope(Dispatchers.IO).launch {
            repository = AlarmRepository(alarmDao)
            _Alarm.postValue(repository.findAlarm(-1L))
        }
    }

    val currentAlarm : LiveData<Alarm?>
        get() = _Alarm

    fun findAlarmInfo(alarmId : Long) {
        viewModelScope.launch(Dispatchers.IO) {
            val alarm : Alarm? = repository.findAlarm(alarmId)
            _Alarm.postValue(alarm)
        }
    }
}


class GroupDetailsViewModelFactory(private val application: Application) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        @Suppress("UNCHECKED_CAST")
        if (modelClass.isAssignableFrom(GroupDetailsViewModel::class.java)) {
            return GroupDetailsViewModel(application) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}