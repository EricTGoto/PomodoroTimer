package com.example.pomodorotimer.timer

import android.media.AudioAttributes
import android.media.AudioManager
import android.media.SoundPool
import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.pomodorotimer.R
import com.example.pomodorotimer.databinding.FragmentTimerBinding
import kotlinx.android.synthetic.main.fragment_timer.*

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [timerFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class timerFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    private lateinit var viewModel: TimerViewModel
    private lateinit var binding: FragmentTimerBinding
    private lateinit var soundPool: SoundPool
    private lateinit var audioAttributes: AudioAttributes

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_timer, container, false)

        // create an application context
        val application = requireNotNull(this.activity).application

        // creates a soundpool object based on the user's APK version
        (if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            audioAttributes =
                AudioAttributes.Builder()
                    .setUsage(AudioAttributes.USAGE_GAME)
                    .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                    .build()
            soundPool =
                SoundPool.Builder().setMaxStreams(1).setAudioAttributes(audioAttributes).build()
        } else soundPool = SoundPool(1, AudioManager.STREAM_MUSIC, 0))

        val viewModelFactory = TimerViewModelFactory(soundPool, application)

        viewModel = ViewModelProvider(this, viewModelFactory).get(TimerViewModel::class.java)

        binding.timerViewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner

        return binding.root
    }

    override fun onPause() {
        super.onPause()
        soundPool.release()
    }

    override fun onDestroy() {
        super.onDestroy()

    }
}