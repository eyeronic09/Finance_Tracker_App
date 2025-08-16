package com.example.financetracker

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.lifecycle.ViewModelProvider
import com.example.financetracker.HomeScreen.HomeScreen
import com.example.financetracker.HomeScreen.TransactionRoom.TransactionDatabase
import com.example.financetracker.HomeScreen.TranscationViewModel
import com.example.financetracker.HomeScreen.TranscationViewModelFactory
import com.example.financetracker.ui.theme.FinanceTrackerTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        // Get the database instance
        val database = TransactionDatabase.getDatabase(this)
        
        // Get the ViewModel with the factory
        val viewModel = ViewModelProvider(
            this,
            TranscationViewModelFactory(database.transactionDao())
        )[TranscationViewModel::class.java]

        enableEdgeToEdge()
        setContent {
            FinanceTrackerTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    HomeScreen(viewModel = viewModel)
                }
            }
        }
    }
}
