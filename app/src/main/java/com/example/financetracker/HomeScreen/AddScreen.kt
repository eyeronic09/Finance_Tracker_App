package com.example.financetracker.HomeScreen

import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.room.Transaction
import androidx.room.util.copy
import com.example.financetracker.HomeScreen.component.addContent



@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddScreen(
    navController: NavController,
    viewModel: TranscationViewModel,

) {

    val amount by viewModel.amount.collectAsState(initial = "")
    val selectedOption by viewModel.selectedOption.collectAsState()
    Scaffold(
        topBar = {
          TopAppBar(
              title = { Text("Add Screen") },
              navigationIcon = {
                  IconButton(onClick = {navController.popBackStack()}) {
                      Icon(
                          imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                          contentDescription = "Localized description"
                      )
                  }
              },

          )
        },
    ) {  innerPadding ->
        addContent(
            amount = amount,
            selectedOption = selectedOption,
            onAmountChange = { viewModel.numField(it) },
            onOptionSelected = { viewModel.onOptionSelected(it) },
            onAddClick = {
                if (amount.isNotBlank() && selectedOption != null) {
                    viewModel.addTransaction()

                }
            },

            modifier = Modifier.padding(innerPadding)
        )
    }
}