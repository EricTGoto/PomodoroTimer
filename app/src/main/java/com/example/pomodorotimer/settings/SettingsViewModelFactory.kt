package com.example.pomodorotimer.settings

import android.app.Application
import androidx.lifecycle.SavedStateViewModelFactory
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.savedstate.SavedStateRegistryOwner
import java.lang.IllegalArgumentException

class SettingsViewModelFactory(application: Application) : ViewModelProvider.Factory{

    val application=application

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(SettingsViewModel::class.java)){
            return SettingsViewModel(application) as T
        }
        throw IllegalArgumentException("Invalid ViewModel class")
    }

}