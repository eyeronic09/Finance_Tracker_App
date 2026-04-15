package com.example.financetracker.AddTransaction.compontent

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.financetracker.AddTransaction.TransactionType

@Composable
fun TransactionTypeRadioButtons(
    selectedRadioButton : TransactionType,
    onClick : (TransactionType) -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(CircleShape)
            .background(color = MaterialTheme.colorScheme.surfaceContainerHighest)
            .padding(4.dp),
        horizontalArrangement = Arrangement.spacedBy(4.dp)
        ) {
        TransactionTypeItem(
            text = "Income",
            isSelected = selectedRadioButton == TransactionType.income,
            onClick = { onClick(TransactionType.income)},
            modifier = Modifier.weight(1f),
            selectedColor = Color(0xFF4CAF50) // Green for Income
        )
        TransactionTypeItem(
            text = "Expense",
            isSelected = selectedRadioButton == TransactionType.expense,
            onClick = { onClick(TransactionType.expense)},
            modifier = Modifier.weight(1f),
            selectedColor = MaterialTheme.colorScheme.error // Red for Expense
        )
    }

}

@Composable()
fun TransactionTypeItem(
    text : String,
    isSelected : Boolean,
    onClick : () -> Unit,
    modifier: Modifier,
    selectedColor: Color
) {
    val containerColor = if (isSelected) selectedColor else Color.Transparent
    val contentColor = if (isSelected) Color.White else MaterialTheme.colorScheme.onSurfaceVariant

    Box(modifier = modifier
        .clip(CircleShape)
        .background(color = containerColor)
        .clickable { onClick() }
        .padding(vertical = 12.dp),
        contentAlignment = Alignment.Center){
        Text(
            text = text,
            style = MaterialTheme.typography.labelLarge.copy(fontWeight = FontWeight.SemiBold),
            color = contentColor
        )
    }
}
