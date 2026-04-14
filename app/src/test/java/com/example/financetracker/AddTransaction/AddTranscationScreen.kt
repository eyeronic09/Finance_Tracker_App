package com.example.financetracker.AddTransaction

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import cafe.adriel.voyager.core.screen.Screen
import org.koin.androidx.compose.koinViewModel

class AddTranscationScreen : Screen {
    @Composable
    override fun Content() {
        AddTransactionScreenRoute()
    }
}

@Composable
fun AddTransactionScreenRoute(viewModel: AddTransactionVM = koinViewModel()){
    AddTranscationScreen()
}
@Composable
fun AddTranscationScreen(viewModel: AddTransactionVM, modifier: Modifier = Modifier) {

}