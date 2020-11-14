package com.example.dice.settings

import android.content.Context
import android.os.Build
import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatDelegate
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.navArgs
import com.example.dice.R
//import com.example.dice.database.Settings
//import com.example.dice.database.SettingsDatabase
import com.example.dice.databinding.FragmentSettingsBinding
//import com.example.dice.home.HomeViewModel
//import com.example.dice.home.HomeViewModelFactory
import kotlinx.coroutines.*

class SettingsFragment : Fragment() {

    private lateinit var binding: FragmentSettingsBinding

//    private lateinit var viewModelFactory: SettingsViewModelFactory
//    private lateinit var viewModel: SettingsViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

//        val database = SettingsDatabase.createInstance(this.requireActivity().application)
//        val dao = database.settingsDao

//        viewModelFactory = SettingsViewModelFactory(dao)
//        viewModel = ViewModelProvider(this, viewModelFactory).get(SettingsViewModel::class.java)

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_settings, container, false)

//        val settings by navArgs<SettingsFragmentArgs>()

        val animationSwitch = binding.sAnimation
        val soundSwitch = binding.sSound
        val vibrationSwitch = binding.sVibration
//        val themeSwitch = binding.sDarkMode
        val preferencesKey = getString(R.string.preferences_key)

        val animationPreferenceKey = getString(R.string.animation_preference)
        val soundPreferenceKey = getString(R.string.sound_preference)
        val vibrationPreferenceKey = getString(R.string.vibration_preference)

        val preferences = activity?.getSharedPreferences(preferencesKey, Context.MODE_PRIVATE)

        val isAnimationOn = preferences?.getBoolean(animationPreferenceKey, true)
        val isSoundOn = preferences?.getBoolean(soundPreferenceKey, true)
        val isVibrationOn =
            preferences?.getBoolean(vibrationPreferenceKey, Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)

        animationSwitch.isChecked = isAnimationOn!!
        soundSwitch.isChecked = isSoundOn!!
        vibrationSwitch.isChecked = isVibrationOn!!
//        themeSwitch.isChecked = settings.darkMode

        animationSwitch.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                with (preferences.edit()) {
                    this?.putBoolean(animationPreferenceKey, true)
                    this?.apply()
                }
            } else {
                with(preferences.edit()) {
                    this?.putBoolean(animationPreferenceKey, false)
                    this?.apply()
                }
            }
        }

        soundSwitch.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                with (preferences.edit()) {
                    this?.putBoolean(soundPreferenceKey, true)
                    this?.apply()
                }
            } else {
                with (preferences.edit()) {
                    this?.putBoolean(soundPreferenceKey, false)
                    this?.apply()
                }
            }
        }

        vibrationSwitch.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    with (preferences.edit()) {
                        this?.putBoolean(vibrationPreferenceKey, true)
                        this?.apply()
                    }
                } else {
                    Toast.makeText(activity, "This feature is not supported by your Android version.", Toast.LENGTH_LONG).show()
                    vibrationSwitch.isChecked = false
                }
            } else {
                with (preferences.edit()) {
                    this?.putBoolean(vibrationPreferenceKey, false)
                    this?.apply()
                }
            }
        }

//        themeSwitch.setOnCheckedChangeListener { _, isChecked ->
//            if (isChecked) {
//                viewModel.updateThemeSettings(true)
//                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
//            } else {
//                viewModel.updateThemeSettings(false)
//                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
//            }
//        }

        return binding.root
    }
}