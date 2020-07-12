package com.example.pomodorotimer.settings

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import android.os.Build
import android.util.Log
import android.widget.RadioGroup
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel


class SettingsViewModel(application: Application) : ViewModel() {

    private val _fruitSelected = MutableLiveData<Int>()
    val fruitSelected: LiveData<Int>
        get() = _fruitSelected

    private val _pineappleId = MutableLiveData<Int>()
    val pineappleId: LiveData<Int>
        get() = _pineappleId


    private val _blueberryId = MutableLiveData<Int>()
    val blueberryId: LiveData<Int>
        get() = _blueberryId

    private val sharedPref: SharedPreferences =
        application.getSharedPreferences("savedSettings", Context.MODE_PRIVATE)

    private val editor : SharedPreferences.Editor = sharedPref.edit()

    fun changeTheme(checkedId: Int) {
        _fruitSelected.value = checkedId
    }

    fun setPineAppleId(Id: Int) {
        _pineappleId.value = Id
    }

    fun setBlueberryId(Id: Int) {
        _blueberryId.value = Id
    }

    fun saveSettings() {
        Log.i("pomo", "settings saved")
        editor.apply {
            putInt("themeId", _fruitSelected.value?:sharedPref.getInt("themeId",0))
            apply()
        }
    }

    override fun onCleared() {
        super.onCleared()
        Log.i("pomo", "oncleared called")
    }
}