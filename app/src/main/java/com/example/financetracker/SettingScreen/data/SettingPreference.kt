package com.example.financetracker.SettingScreen.data

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import org.koin.core.context.startKoin

val Context.CurrencyPrefs: DataStore<Preferences> by preferencesDataStore(name = "currency_pref")
val Context.ThemePrefs: DataStore<Preferences> by preferencesDataStore(name = "theme_pref")

object DataStoreKey{
    val defaultCurrency = stringPreferencesKey("currency")
    val themeSetting = stringPreferencesKey("theme_setting")
}