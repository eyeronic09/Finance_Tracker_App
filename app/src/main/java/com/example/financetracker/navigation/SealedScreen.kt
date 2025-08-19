package com.example.financetracker.navigation

import kotlinx.serialization.Serializable


sealed class SealedScreen(val route : String){

    object HomeScreen : SealedScreen("HomeScreen")

    object AddScreen : SealedScreen("AddScreen" )
}
