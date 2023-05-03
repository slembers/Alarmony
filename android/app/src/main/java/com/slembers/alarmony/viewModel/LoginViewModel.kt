package com.slembers.alarmony.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class LoginViewModel(
    private val _accessToken : MutableLiveData<String> = MutableLiveData(""),
    private val _refreshToken : MutableLiveData<String> = MutableLiveData(""),
) : ViewModel() {

    val access : LiveData<String> = _accessToken
    val refresh : LiveData<String> = _refreshToken

    fun onChangeToken(token : String) {
        _accessToken.value = token
    }

    fun setToken(access : String, refresh : String) {
        _accessToken.value = "Bearer " + access
        _refreshToken.value = "Bearer " + refresh
    }


}