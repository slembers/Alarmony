package com.slembers.alarmony.viewModel

import android.app.Application
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.slembers.alarmony.feature.alarm.Alarm
import com.slembers.alarmony.feature.alarm.AlarmDatabase
import com.slembers.alarmony.feature.alarm.AlarmRepository
import com.slembers.alarmony.model.db.Record
import com.slembers.alarmony.network.service.GroupService
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.launch
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

@ExperimentalCoroutinesApi
class GroupDetailsViewModel (
    application: Application
) : ViewModel() {

    private val repository : AlarmRepository

    init {
        Log.d("groupViewModel","VIewModel 초기화!!")
        val alarmDao = AlarmDatabase.getInstance(application).alarmDao()
        repository = AlarmRepository(alarmDao)
    }

    suspend fun getRecord(alarmId : Long) : Map<String, List<Record>> = suspendCoroutine { continuation ->
        viewModelScope.launch {
            val result = GroupService.getGroupRecord(alarmId)
            continuation.resume(result)
        }
    }

    suspend fun getAlarm(alarmId : Long) : Alarm? = suspendCoroutine { continuation ->
        viewModelScope.launch {
            val result = repository.findAlarm(alarmId)
            continuation.resume(result)
        }
    }

    class GroupViewModelFactory(
        private val application: Application
    ) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            @Suppress("UNCHECKED_CAST")
            if (modelClass.isAssignableFrom(GroupDetailsViewModel::class.java)) {
                return GroupDetailsViewModel(application) as T
            }
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }

    override fun onCleared() {
        super.onCleared()
    }
}