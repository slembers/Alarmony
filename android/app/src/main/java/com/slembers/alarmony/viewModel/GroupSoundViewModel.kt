package com.slembers.alarmony.viewModel

import android.app.Application
import android.util.Log
import androidx.compose.runtime.mutableStateMapOf
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.slembers.alarmony.R
import com.slembers.alarmony.util.Sound


class GroupSoundViewModel(application: Application) : ViewModel() {
    private val _soundItems : MutableMap<String, Sound> = mutableStateMapOf()
    private val _soundItemList : MutableLiveData<List<Sound>> = MutableLiveData(listOf())

    val soundItemList : LiveData<List<Sound>>
        get() = _soundItemList

    fun updateSoundItemAll(soundAll : Map<String, Sound>) {
        _soundItems.putAll(soundAll)
        _soundItemList.postValue(_soundItems.values.toList())
    }

    fun updateSoundPlay(soundName : String) {
        _soundItems[soundName]?.onchangeImage(R.drawable.pause_button)
        _soundItemList.postValue(_soundItems.values.toList())
    }

    fun updateSoundStop(soundName : String) {
        _soundItems.get(soundName)?.onchangeImage(R.drawable.play_button)
        _soundItemList.postValue(_soundItems.values.toList())
    }

    class GroupSoundViewModelFactory(
        private val application: Application
    ) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            @Suppress("UNCHECKED_CAST")
            if (modelClass.isAssignableFrom(GroupSoundViewModel::class.java)) {
                return GroupSoundViewModel(application) as T
            }
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
}