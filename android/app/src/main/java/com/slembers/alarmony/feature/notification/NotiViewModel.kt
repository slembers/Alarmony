package com.slembers.alarmony.feature.notification

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class NotiViewModel(application: Application) : AndroidViewModel(application) {
    val readAllData: LiveData<List<Noti>>
    private val repository: NotiRepository

    init {
        val notiDao = NotiDatabase.getInstance(application).notiDao()
        repository = NotiRepository(notiDao)
        readAllData = repository.readAllData
    }

    fun addNoti(noti: Noti) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.addNoti(noti)
        }
    }

    fun deleteNoti(noti: Noti) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.deleteNoti(noti)
        }
    }

    fun deleteAllNoti() {
        viewModelScope.launch(Dispatchers.IO) {
            repository.deleteAllNotis()
        }
    }
}

class NotiViewModelFactory(private val application: Application) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        @Suppress("UNCHECKED_CAST")
        if (modelClass.isAssignableFrom(NotiViewModel::class.java)) {
            return NotiViewModel(application) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}