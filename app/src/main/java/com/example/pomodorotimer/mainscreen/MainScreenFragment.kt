package com.example.pomodorotimer.mainscreen

import android.content.Context
import android.media.AudioAttributes
import android.media.AudioManager
import android.media.SoundPool
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.fragment.app.Fragment
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.NavigationUI

import com.example.pomodorotimer.R
import com.example.pomodorotimer.databinding.FragmentMainScreenBinding
import com.example.pomodorotimer.settings.SettingsViewModel
import com.example.pomodorotimer.settings.SettingsViewModelFactory

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [MainScreenFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class MainScreenFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null


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
        Log.i("pomo","oncreate view called")
        val binding: FragmentMainScreenBinding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_main_screen, container, false)

        val application = requireNotNull(this.activity).application
        setHasOptionsMenu(true)
        val viewModelFactory = MainScreenViewModelFactory()
        val mainScreenViewModel =
            ViewModelProvider(this, viewModelFactory).get(MainScreenViewModel::class.java)

        val settingsModelFactory = SettingsViewModelFactory(application)
        val settingsViewModel =
            ViewModelProvider(this, settingsModelFactory).get(SettingsViewModel::class.java)
        binding.lifecycleOwner = this
        binding.mainScreenViewModel = mainScreenViewModel

        val sharedPref = application.getSharedPreferences("savedSettings", Context.MODE_PRIVATE)

        val themeId = sharedPref.getInt("themeId", 0)
        Log.i("pomo", "themeId main screen $themeId")
        if (themeId == sharedPref.getInt("blueberryId", 0)) {
            binding.ivFruit.setImageResource(R.mipmap.ic_blueberry)
        } else {
            binding.ivFruit.setImageResource(R.drawable.ic_pineapple)
        }


        mainScreenViewModel.navigateToTimer.observe(
            viewLifecycleOwner,
            Observer<Boolean> { toTimer ->
                if (toTimer) {
                    this.findNavController()
                        .navigate(MainScreenFragmentDirections.actionMainScreenFragmentToTimerFragment())
                    mainScreenViewModel.wentToTimer()
                }
            })

        mainScreenViewModel.navigateToAbout.observe(viewLifecycleOwner, Observer { toAbout ->
            if (toAbout) {
                this.findNavController()
                    .navigate(MainScreenFragmentDirections.actionMainScreenFragmentToAboutFragment())
                mainScreenViewModel.wentToAbout()
            }
        })

        mainScreenViewModel.navigateToSettings.observe(viewLifecycleOwner, Observer { toSettings ->
            if (toSettings) {
                this.findNavController()
                    .navigate(MainScreenFragmentDirections.actionMainScreenFragmentToSettingsFragment())
                mainScreenViewModel.wentToSettings()
            }
        })

        // if (settingsViewModel.fruitSelected.value==settingsViewModel.blueberryId.)

        return binding.root
    }
    override fun onPause() {
        super.onPause()
        Log.i("pomo","onpause called")
    }
    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater?.inflate(R.menu.options_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return NavigationUI.onNavDestinationSelected(
            item!!,
            requireView().findNavController()
        ) || super.onOptionsItemSelected(item)
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment MainScreenFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            MainScreenFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}