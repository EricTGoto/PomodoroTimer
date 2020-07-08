package com.example.pomodorotimer.timer

import android.app.Application
import android.media.AudioAttributes
import android.media.AudioManager
import android.media.MediaPlayer
import android.media.SoundPool
import android.os.Build
import android.os.CountDownTimer
import android.text.format.DateUtils
import android.util.Log
import android.view.View
import androidx.lifecycle.*
import com.example.pomodorotimer.R
import kotlinx.coroutines.*

class TimerViewModel(soundPool: SoundPool, application: Application) :
    AndroidViewModel(application) {

    // Copies of argument variables
    private var soundPoolCopy = soundPool
    private var applicationCopy = application

    // Variables related to the timer
    private var timer: CountDownTimer
    private val _current_time = MutableLiveData<Long>()
    val current_time: LiveData<Long>
        get() = _current_time
    val currentTimeString = Transformations.map(current_time) { time ->
        DateUtils.formatElapsedTime(time)
    }
    private var isBreak: Boolean = false
    private var pomoCount: Int = 0


    // Variables related to the resume/pause button
    private var visibilityGone = View.GONE
    private var visiblityVisible = View.VISIBLE
    private val _pauseButtonVisibility = MutableLiveData<Int>()
    val pauseButtonVisibility: LiveData<Int>
        get() = _pauseButtonVisibility
    private val _resumeButtonVisibility = MutableLiveData<Int>()
    val resumeButtonVisibility: LiveData<Int>
        get() = _resumeButtonVisibility

    private val _isPaused = MutableLiveData<Boolean>()
    val isPaused: LiveData<Boolean>
        get() = _isPaused

    // Coroutine variables
    private var viewModelJob = Job()
    private val uiScope = CoroutineScope(Dispatchers.Main + viewModelJob)


    var soundID: Int=0

    companion object {
        private const val DONE = 0L

        private const val ONE_SECOND = 1000L

        private const val POMO_TIME = 10000L

        private const val LONG_BREAK = 5000L

        private const val SHORT_BREAK = 3000L
    }

    init {
        _isPaused.value = false
        _resumeButtonVisibility.value = visibilityGone

        viewModelScope.launch{
           soundID= soundPool.load(application, R.raw.alarmshort, 0)
        }
        timer = object : CountDownTimer(POMO_TIME, ONE_SECOND) {
            override fun onTick(millisUntilFinished: Long) {
                _current_time.value = millisUntilFinished / ONE_SECOND
            }

            override fun onFinish() {
                //soundPool.play(soundID, 1f, 1f, 1, 0, 1f)
                uiScope.launch {
                    withContext(Dispatchers.IO) {
                        soundPool.play(soundID, 1f, 1f, 1, 0, 1f)
                    }
                }
                pomoCount++
                shortBreak()
            }
        }
        timer.start()
    }

    private fun longBreak() {
        isBreak = true
        createTimer(LONG_BREAK)
    }

    private fun shortBreak() {
        isBreak = true
        createTimer(SHORT_BREAK)
    }

    fun onPause() {
        _isPaused.value = true
        _pauseButtonVisibility.value = visibilityGone
        _resumeButtonVisibility.value = visiblityVisible
        timer.cancel()
    }

    fun onResumed() {
        _resumeButtonVisibility.value = visibilityGone
        _pauseButtonVisibility.value = visiblityVisible
        _isPaused.value = false

        // Create a new timer object so that the correct time can be displayed
        createTimer(_current_time.value?.times(ONE_SECOND)!!)
    }

    private fun createTimer(time: Long) {
        timer = object : CountDownTimer(time, ONE_SECOND) {
            override fun onTick(millisUntilFinished: Long) {
                _current_time.value = millisUntilFinished / ONE_SECOND
            }

            override fun onFinish() {
                uiScope.launch {
                    withContext(Dispatchers.IO) {
                        playSound(soundPoolCopy, applicationCopy)
                    }
                }

                if (isBreak) {
                    isBreak = false
                    createTimer(POMO_TIME)
                    pomoCount++
                } else {
                    if (pomoCount % 4 == 0) longBreak()
                    else shortBreak()
                }
            }
        }
        timer.start()
    }

    // Loads the alarm sound and then plays it
    private fun playSound(soundPool: SoundPool, application: Application) {
        soundPool.load(application, R.raw.alarmshort, 0)
        soundPool.setOnLoadCompleteListener { soundPool, soundId, p2 ->
            if (p2 == 0) {
                soundPool.play(soundId, 1f, 1f, 1, 0, 1f)
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }
}