package com.example.pomodorotimer.mainscreen

import androidx.databinding.DataBindingUtil
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class MainScreenViewModel : ViewModel() {


    private val _navigateToTimer = MutableLiveData<Boolean>()
    val navigateToTimer: LiveData<Boolean>
        get() = _navigateToTimer

    private val _navigateToSettings = MutableLiveData<Boolean>()
    val navigateToSettings: LiveData<Boolean>
        get() = _navigateToSettings

    private val _navigateToAbout = MutableLiveData<Boolean>()
    val navigateToAbout: LiveData<Boolean>
        get() = _navigateToAbout

    fun wentToTimer() {
        _navigateToTimer.value = false
    }

    fun onStart() {
        _navigateToTimer.value = true
    }

    fun onAbout() {
        _navigateToAbout.value = true
    }

    fun wentToAbout(){
        _navigateToAbout.value=false
    }

    fun onSettings() {
        _navigateToSettings.value = true
    }

    fun wentToSettings() {
        _navigateToSettings.value = false
    }
}