package com.slembers.alarmony.model.db

import androidx.compose.ui.graphics.painter.Painter
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class SoundModel(private var soundItems : MutableLiveData<ArrayList<SoundItem>>) : ViewModel() {

    var items = soundItems

    fun addSoundList(items : ArrayList<SoundItem>) {
        soundItems.value = items
    }
}

data class SoundItem(
    private val image : Painter? = null,
    private val name : String = "nothing"
) {
    var soundImage = image
    var soundName = name

    fun onChangeImage(image : Painter?) {
        soundImage = image
    }
}