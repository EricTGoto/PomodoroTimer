package com.example.pomodorotimer.mainscreen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import java.lang.IllegalArgumentException

class MainScreenViewModelFactory : ViewModelProvider.Factory{
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MainScreenViewModel::class.java)){
            return MainScreenViewModel() as T
        }
        throw IllegalArgumentException("Unknown viewmodel class")
    }
}