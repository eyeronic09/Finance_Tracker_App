package com.example.financetracker.HomeScreen.component


import android.os.Message
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.DialogTitle
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.AlertDialogDefaults
import androidx.compose.material3.BasicAlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.room.Delete
import com.example.financetracker.ui.theme.FinanceTrackerTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Dialogs(modifier: Modifier ,title: String , message: String, onCancel : () -> Unit , onDelete: () -> Unit){
    BasicAlertDialog(
        modifier = modifier,
        onDismissRequest = {onCancel()}
    ){
        Surface (modifier = Modifier.fillMaxWidth(),
            tonalElevation = AlertDialogDefaults.TonalElevation,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            shape = RoundedCornerShape(12.dp)
        ){
            Column(
                modifier = Modifier.padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(24.dp)
            ) {
                Text(title)
                Row (modifier.fillMaxWidth() ,
                    verticalAlignment = Alignment.CenterVertically) {
                    Button(onCancel) {
                        Text("cancel")
                    }
                    Button(onDelete) {
                        Text("confirm")
                    }

                }

            }

        }
    }
}

@Preview(showSystemUi = true)
@Composable
private fun DialogsPreview() {
    FinanceTrackerTheme {
        Dialogs(
            modifier = Modifier,
            title = "Delete transaction?",
            message = "This action cannot be undone.",
            onCancel = {},
            onDelete = {}
        )
    }
}
