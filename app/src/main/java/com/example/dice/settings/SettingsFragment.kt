package com.example.dice.settings

import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.example.dice.R
import com.example.dice.database.Settings
import com.example.dice.database.SettingsDatabase
import com.example.dice.databinding.FragmentSettingsBinding
import kotlinx.coroutines.*

class SettingsFragment : Fragment() {

    private lateinit var binding: FragmentSettingsBinding
    private lateinit var viewModel: SettingsViewModel

//    val job = Job()
//    val scope = CoroutineScope(Dispatchers.Main + job)
//    val database = SettingsDatabase.createInstance(this.requireActivity().application)
//    val dao = database.settingsDao


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_settings, container, false)


//    launchIO()



        return binding.root
    }

//    fun launchIO() {
//        scope.launch {
//            updateDb()
//        }
//    }
//
//    suspend fun updateDb() {
//        withContext(Dispatchers.IO) {
//            dao.update(Settings(1, binding.sAnimation.isChecked, binding.sDarkMode.isChecked))
//        }
//    }
}