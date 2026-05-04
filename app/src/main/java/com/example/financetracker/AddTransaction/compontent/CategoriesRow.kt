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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Category
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RowScrollDropdown(
    selectedCategory: String,
    onCategorySelected: (String) -> Unit,
    categories: List<String>
) {
    Column {
        Row(
            modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 8.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text("Category", style = MaterialTheme.typography.titleMedium)
        }

        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            contentPadding = PaddingValues(horizontal = 16.dp)
        ) {
            items(categories) { category ->
                val selected = category == selectedCategory
                val icon = getCategoryIcon(category)
                val isDefaultIcon = icon == Icons.Default.Category
                
                val colors = when (category) {
                    "Food", "Food & Dining", "Groceries" -> Pair(Color(0xFFFF9800), Color(0xFFFFF3E0))
                    "Shopping" -> Pair(Color(0xFF2196F3), Color(0xFFE3F2FD))
                    "Transportation" -> Pair(Color(0xFF9C27B0), Color(0xFFF3E5F5))
                    "Movies", "Entertainment" -> Pair(Color(0xFFE91E63), Color(0xFFFCE4EC))
                    "Salary" -> Pair(Color(0xFF4CAF50), Color(0xFFE8F5E9))
                    "Investment" -> Pair(Color(0xFF00FF0E), Color(0xFFE0F7FA))
                    "Rent" -> Pair(Color(0xFF795548), Color(0xFFEFEBE9))
                    "Bills & Utilities" -> Pair(Color(0xFF673AB7), Color(0xFFF3E5F5))
                    "Healthcare" -> Pair(Color(0xFFE91E63), Color(0xFFFCE4EC))
                    "Other Income" -> Pair(Color(0xFF4CAF50), Color(0xFFE8F5E9))
                    "Other Expense" -> Pair(Color(0xFFF60000), Color(0xFFF5F5F5))
                    else -> Pair(MaterialTheme.colorScheme.primary, MaterialTheme.colorScheme.primaryContainer)
                }

                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.clickable { onCategorySelected(category) }
                ) {
                    Box(
                        modifier = Modifier
                            .size(64.dp)
                            .background(
                                color = if (selected) colors.first.copy(alpha = 0.2f) else colors.second,
                                shape = RoundedCornerShape(16.dp)
                            )
                            .border(
                                width = if (selected) 2.dp else 0.dp,
                                color = if (selected) colors.first else Color.Transparent,
                                shape = RoundedCornerShape(16.dp)
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        if (isDefaultIcon && category.isNotEmpty()) {
                            // Show first letter of category if it's a generic icon
                            Text(
                                text = category.take(1).uppercase(),
                                style = MaterialTheme.typography.titleLarge,
                                fontWeight = FontWeight.Bold,
                                color = colors.first
                            )
                        } else {
                            Icon(
                                imageVector = icon,
                                contentDescription = category,
                                tint = colors.first,
                                modifier = Modifier.size(30.dp)
                            )
                        }
                    }
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = category,
                        style = MaterialTheme.typography.labelSmall,
                        color = if (selected) colors.first else MaterialTheme.colorScheme.onSurfaceVariant,
                        maxLines = 1
                    )
                }
            }
        }
    }
}
