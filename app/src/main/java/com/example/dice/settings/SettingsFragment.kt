package com.example.dice.settings

import android.content.Context
import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import com.example.dice.R
import com.example.dice.databinding.FragmentSettingsBinding


class SettingsFragment : Fragment() {

    private lateinit var binding: FragmentSettingsBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_settings, container, false)

        val animationSwitch = binding.sAnimation
        val soundSwitch = binding.sSound
        val vibrationSwitch = binding.sVibration
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

        return binding.root
    }
}