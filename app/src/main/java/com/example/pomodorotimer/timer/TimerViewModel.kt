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

class TimerViewModel(soundPool: SoundPool, application: Application) :
    AndroidViewModel(application) {

    private var timer: CountDownTimer

    private val _current_time = MutableLiveData<Long>()
    val current_time: LiveData<Long>
        get() = _current_time

    val currentTimeString = Transformations.map(current_time) { time ->
        DateUtils.formatElapsedTime(time)
    }

    private val isShortBreak = MutableLiveData<Boolean>()
    private val _isPaused = MutableLiveData<Boolean>()
    val isPaused: LiveData<Boolean>
        get() = _isPaused

    private val _isResumed = MutableLiveData<Boolean>()
    val isResumed: LiveData<Boolean>
        get() = _isResumed

    private val isLongBreak = MutableLiveData<Boolean>()
    private val isPomo = MutableLiveData<Boolean>()


    private var visibilityGone = View.GONE
    private var visiblityVisible = View.VISIBLE
    private val _pauseButtonVisibility = MutableLiveData<Int>()
    val pauseButtonVisibility: LiveData<Int>
        get() = _pauseButtonVisibility

    private val _resumeButtonVisibility = MutableLiveData<Int>()
    val resumeButtonVisibility: LiveData<Int>
        get() = _resumeButtonVisibility

    private var soundPoolCopy=soundPool
    private var applicationCopy= application

    companion object {
        private const val DONE = 0L

        private const val ONE_SECOND = 1000L

        private const val POMO_TIME = 3000L
    }

    init {
        _isPaused.value = false
        _resumeButtonVisibility.value = visibilityGone

        timer = object : CountDownTimer(POMO_TIME, ONE_SECOND) {
            override fun onTick(millisUntilFinished: Long) {
                _current_time.value = millisUntilFinished / ONE_SECOND
            }

            override fun onFinish() {
                playSound(soundPool, application)
                _current_time.value = DONE
            }
        }
        timer.start()
    }

    fun onPomoFinish() {

    }

    fun onPause() {
        _isPaused.value = true
        _pauseButtonVisibility.value = visibilityGone
        _resumeButtonVisibility.value = visiblityVisible
        _isResumed.value = false
        timer.cancel()
    }

    fun onResumed() {
        _isResumed.value = true
        _resumeButtonVisibility.value = visibilityGone
        _pauseButtonVisibility.value = visiblityVisible
        _isPaused.value = false

        // Create a new timer object so that the correct time can be displayed
        timer = object : CountDownTimer(_current_time.value?.times(ONE_SECOND)!!, ONE_SECOND) {
            override fun onTick(millisUntilFinished: Long) {
                _current_time.value = millisUntilFinished / ONE_SECOND
            }

            override fun onFinish() {
                _current_time.value = DONE
                playSound(soundPoolCopy, applicationCopy)
                //soundPool.play(sound1, 1F, 1F, 0, 0, 1.0F)
                Log.i("soundPLayed", "sound was played")
            }
        }
        timer.start()
    }

    // Loads the alarm sound and then plays it
    private fun playSound(soundPool: SoundPool, application: Application) {
        soundPool.load(application, R.raw.alarm, 0)
        soundPool.setOnLoadCompleteListener { soundPool, soundId, p2 ->
            if (p2 == 0) {
                soundPool.play(soundId, 1f, 1f, 1, 1, 1f)
            }
        }
    }

}