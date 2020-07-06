package com.example.pomodorotimer.timer

import android.app.Application
import android.media.SoundPool
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import java.lang.IllegalArgumentException

class TimerViewModelFactory(
    private val soundPool: SoundPool,
    private val application: Application
) : ViewModelProvider.Factory {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(TimerViewModel::class.java)) {
            return TimerViewModel(soundPool, application) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}