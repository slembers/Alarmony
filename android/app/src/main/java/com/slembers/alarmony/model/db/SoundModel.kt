package com.slembers.alarmony.model.db

import androidx.compose.ui.graphics.painter.Painter
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

interface SoundModelInterface {
    var items : ArrayList<SoundItem>?
}

class SoundModel(private val soundItems : MutableLiveData<ArrayList<SoundItem>>) : ViewModel() , SoundModelInterface {

    override var items : ArrayList<SoundItem>? = soundItems.value

    fun addSoundList(item : SoundItem) {
        soundItems.value?.add(item)
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