package com.slembers.alarmony.feature.ui.groupDetails

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel

class SendAlarmButtonViewModel() : ViewModel() {
    var onAlarm: MutableState<Boolean> = mutableStateOf(true)
    var remainingSec: MutableState<Int> = mutableStateOf(60)
}