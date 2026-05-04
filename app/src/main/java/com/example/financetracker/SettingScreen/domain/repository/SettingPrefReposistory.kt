package com.example.financetracker.SettingScreen.domain.repository

import android.content.Context
import androidx.datastore.preferences.core.edit
import com.example.financetracker.SettingScreen.data.CurrencyPrefs
import com.example.financetracker.SettingScreen.data.DataStoreKey
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.map

class SettingPrefReposistory(private val context: Context) {

    suspend fun saveDefaultCurrency(currency: String) {
        context.CurrencyPrefs.edit { preferences ->
            preferences[DataStoreKey.defaultCurrency] = currency
        }
    }

    fun getCurrency(): Flow<String> {
        return context.CurrencyPrefs.data.map { preferences ->
            preferences[DataStoreKey.defaultCurrency] ?: "rupee"
        }
    }

    suspend fun saveThemeSetting(theme: String) {
        context.CurrencyPrefs.edit { preferences ->
            preferences[DataStoreKey.themeSetting] = theme
        }
    }

    fun getThemeSetting(): Flow<String> {
        return context.CurrencyPrefs.data.map { preferences ->
            preferences[DataStoreKey.themeSetting] ?: "System Default"
        }
    }
}