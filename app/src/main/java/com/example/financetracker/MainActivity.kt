package com.example.financetracker

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.financetracker.HomeScreen.AddScreen
import com.example.financetracker.HomeScreen.HomeScreen
import com.example.financetracker.HomeScreen.TransactionRoom.TransactionDatabase
import com.example.financetracker.HomeScreen.TranscationViewModel
import com.example.financetracker.HomeScreen.TranscationViewModelFactory
import com.example.financetracker.navigation.SealedScreen
import com.example.financetracker.ui.theme.FinanceTrackerTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        enableEdgeToEdge()
        setContent {
            FinanceTrackerTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    AppNav()
                }
            }
        }
    }
}

@Composable
fun AppNav() {
    val navController = rememberNavController()
    val context = LocalContext.current
    val database = TransactionDatabase.getDatabase(context)
    val viewModel: TranscationViewModel = viewModel(
        factory = TranscationViewModelFactory(database.transactionDao())
    )
    
    NavHost(
        navController = navController,
        startDestination = SealedScreen.HomeScreen.route
    ) {
        composable(SealedScreen.HomeScreen.route) {
            HomeScreen(viewModel = viewModel, navController = navController)
        }
        composable(SealedScreen.AddScreen.route) {
            AddScreen(
                viewModel = viewModel,
                navController = navController
            )
        }
    }
}