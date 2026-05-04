package com.example.financetracker.SettingScreen.Ui.Screen.ViewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.financetracker.SettingScreen.domain.repository.SettingPrefReposistory
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

data class SettingScreenUiState(
    val currencyToChoose: List<String> = listOf("rupee", "usd", "yen"),
    val selectedCurrency: String = "rupee",
    val themeSetting: String = "System Default"
)

class SettingVM(private val settingPrefReposistory: SettingPrefReposistory) : ViewModel() {

    val uiState: StateFlow<SettingScreenUiState> = combine(
        settingPrefReposistory.getCurrency(),
        settingPrefReposistory.getThemeSetting()
    ) { currency, theme ->
        SettingScreenUiState(
            selectedCurrency = currency,
            themeSetting = theme
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.Eagerly,
        initialValue = SettingScreenUiState()
    )

    fun updateCurrency(currency: String) {
        viewModelScope.launch {
            settingPrefReposistory.saveDefaultCurrency(currency)
        }
    }

    fun updateTheme(theme: String) {
        viewModelScope.launch {
            settingPrefReposistory.saveThemeSetting(theme)
        }
    }
}
