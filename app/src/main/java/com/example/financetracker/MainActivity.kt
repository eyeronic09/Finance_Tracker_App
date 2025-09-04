package com.example.financetracker

import android.os.Build
import android.os.Bundle
import android.view.View
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.ViewModel
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
import com.example.financetracker.summaryScreen.SummaryScreen
import com.example.financetracker.summaryScreen.SummaryViewModel
import com.example.financetracker.summaryScreen.SummaryModel.SummaryViewModelFactory
import com.example.financetracker.summaryScreen.component.SummaryCard
import com.example.financetracker.ui.theme.FinanceTrackerTheme

class MainActivity : ComponentActivity() {
    @RequiresApi(Build.VERSION_CODES.O)
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

@RequiresApi(Build.VERSION_CODES.O)
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
        composable(SealedScreen.summaryScreen.route) {
            val viewModel: SummaryViewModel = viewModel(
                factory = SummaryViewModelFactory(database.transactionDao())
            )
            SummaryScreen(
                navController = navController,
                viewModel = viewModel
            )
        }
    }
}