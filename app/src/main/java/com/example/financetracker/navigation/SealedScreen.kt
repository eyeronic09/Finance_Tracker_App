package com.example.financetracker.navigation

import androidx.lifecycle.viewmodel.compose.viewModel

sealed class SealedScreen(val route : String){
    object HomeScreen : SealedScreen("HomeScreen")
    object AddScreen : SealedScreen("AddScreen" )
}
