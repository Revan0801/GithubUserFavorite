package com.example.githubuserfavorite.ui.activity

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.CompoundButton
import androidx.appcompat.app.AppCompatDelegate
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.ViewModelProvider
import com.example.githubuserfavorite.databinding.ActivityDarkModeSettingBinding
import com.example.githubuserfavorite.setting.DataStoreViewModel
import com.example.githubuserfavorite.setting.DataStoreViewModelFactory
import com.example.githubuserfavorite.setting.SettingPreferences
import com.google.android.material.switchmaterial.SwitchMaterial

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

class DarkModeSettingActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDarkModeSettingBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityDarkModeSettingBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.title = "Theme Settings"

        val switchTheme = findViewById<SwitchMaterial>(binding.switchTheme.id)

        val pref = SettingPreferences.getInstance(dataStore)
        val dataStoreViewModel = ViewModelProvider(this, DataStoreViewModelFactory(pref))[DataStoreViewModel::class.java]

        dataStoreViewModel.getThemeSetting().observe(this) { isDarkModeActive: Boolean ->
            if (isDarkModeActive) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                switchTheme.isChecked = true
            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                switchTheme.isChecked = false
            }
        }

        switchTheme.setOnCheckedChangeListener { _: CompoundButton?, isChecked: Boolean ->
            dataStoreViewModel.saveThemeSetting(isChecked)
        }
    }
}