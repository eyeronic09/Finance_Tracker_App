package com.example.financetracker.AddTransaction.compontent

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RowScrollDropdown(
    selectedCategory: String,
    onCategorySelected: (String) -> Unit,
    categories: List<String>
) {
    Column {
        Row(
            modifier = Modifier.fillMaxWidth().padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.Companion.CenterVertically
        ) {
            Text("Category", style = MaterialTheme.typography.titleMedium)
        }

        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            contentPadding = PaddingValues(horizontal = 4.dp)
        ) {
            items(categories) { category ->
                val selected = category == selectedCategory
                val colors = when (category) {
                    "Food" -> Pair(Color(0xFFFF9800), Color(0xFFFFF3E0))
                    "Food & Dining" -> Pair(Color(0xFFFF9800), Color(0xFFFFF3E0))
                    "Shopping" -> Pair(Color(0xFF2196F3), Color(0xFFE3F2FD))
                    "Transportation" -> Pair(Color(0xFF9C27B0), Color(0xFFF3E5F5))
                    "Movies" -> Pair(Color(0xFFE91E63), Color(0xFFFCE4EC))
                    "Salary" -> Pair(Color(0xFF4CAF50), Color(0xFFE8F5E9))
                    "Investment" -> Pair(Color(0xFF00FF0E), Color(0xFFE0F7FA))
                    "Rent" -> Pair(Color(0xFF795548), Color(0xFFEFEBE9))
                    "Bills & Utilities" -> Pair(Color(0xFF673AB7), Color(0xFFF3E5F5))
                    "Healthcare" -> Pair(Color(0xFFE91E63), Color(0xFFFCE4EC))

                    "Other Income" -> Pair(Color(0xFF4CAF50), Color(0xFFE8F5E9))
                    "Other Expense" -> Pair(Color(0xFFF60000), Color(0xFFF5F5F5))
                    else -> Pair(Color(0xFF757575), Color(0xFFF5F5F5))
                }
                Column(
                    horizontalAlignment = Alignment.Companion.CenterHorizontally,
                    modifier = Modifier.Companion.clickable { onCategorySelected(category) }
                ) {
                    Box(
                        modifier = Modifier.Companion
                            .size(60.dp)
                            .background(
                                color = if (selected) colors.first.copy(alpha = 0.2f) else colors.second,
                                shape = RoundedCornerShape(16.dp)
                            )
                            .border(
                                width = if (selected) 2.dp else 0.dp,
                                color = if (selected) colors.first else Color.Companion.Transparent,
                                shape = androidx.compose.foundation.shape.RoundedCornerShape(16.dp)
                            ),
                        contentAlignment = Alignment.Companion.Center
                    ) {
                        Icon(
                            imageVector = getCategoryIcon(category),
                            contentDescription = category,
                            tint = colors.first,
                            modifier = Modifier.Companion.size(28.dp)
                        )
                    }
                    Spacer(modifier = Modifier.Companion.height(4.dp))
                    Text(
                        text = category,
                        style = MaterialTheme.typography.bodySmall,
                        color = if (selected) colors.first else Color.Companion.Gray
                    )
                }
            }
        }
    }
}