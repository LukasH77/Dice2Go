package com.example.dice.settings

import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatDelegate
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.navArgs
import com.example.dice.R
import com.example.dice.database.Settings
import com.example.dice.database.SettingsDatabase
import com.example.dice.databinding.FragmentSettingsBinding
import com.example.dice.home.HomeViewModel
import com.example.dice.home.HomeViewModelFactory
import kotlinx.coroutines.*

class SettingsFragment : Fragment() {

    private lateinit var binding: FragmentSettingsBinding

    private lateinit var viewModelFactory: SettingsViewModelFactory
    private lateinit var viewModel: SettingsViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val database = SettingsDatabase.createInstance(this.requireActivity().application)
        val dao = database.settingsDao

        viewModelFactory = SettingsViewModelFactory(dao)
        viewModel = ViewModelProvider(this, viewModelFactory).get(SettingsViewModel::class.java)

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_settings, container, false)

        val settings by navArgs<SettingsFragmentArgs>()

        val animationSwitch = binding.sAnimation
        val soundSwitch = binding.sSound
        val vibrationSwitch = binding.sVibration
        val themeSwitch = binding.sDarkMode

        animationSwitch.isChecked = settings.animation
        soundSwitch.isChecked = settings.sound
        vibrationSwitch.isChecked = settings.vibration
        themeSwitch.isChecked = settings.darkMode

        themeSwitch.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                viewModel.updateThemeSettings(true)
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            } else {
                viewModel.updateThemeSettings(false)
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            }
        }

        return binding.root
    }
}