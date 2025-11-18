package com.example.f_tracker_kotlin.ui.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.TrendingDown
import androidx.compose.material.icons.automirrored.outlined.TrendingUp
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

// App colors
private val PrimaryGreen = Color(0xFF4ADE80)
private val ExpenseRed = Color(0xFFEF4444)
private val CardBg = Color(0xFF1F1F22) // ⬅️ Lebih gelap dari background tapi tetap beda

@Composable
fun SummarySection(income: Long, expense: Long) {
    val balance = income - expense

    Row(
        horizontalArrangement = Arrangement.spacedBy(10.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        SummaryCard(
            title = "Income",
            amount = income,
            color = PrimaryGreen,
            icon = Icons.AutoMirrored.Outlined.TrendingUp, // ⬅️ Income naik ✔
            modifier = Modifier.weight(1f)
        )
        SummaryCard(
            title = "Expense",
            amount = expense,
            color = ExpenseRed,
            icon = Icons.AutoMirrored.Outlined.TrendingDown, // ⬅️ Expense turun ✔
            modifier = Modifier.weight(1f)
        )
    }
}

@Composable
fun SummaryCard(
    title: String,
    amount: Long,
    color: Color,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = CardBg),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(icon, contentDescription = title, tint = color)

            Spacer(Modifier.width(12.dp))

            Column {
                Text(title, fontWeight = FontWeight.SemiBold, fontSize = 14.sp, color = Color.White)
                Spacer(Modifier.height(4.dp))
                Text(
                    text = "Rp $amount",
                    fontWeight = FontWeight.Bold,
                    color = color,
                    fontSize = 16.sp
                )
            }
        }
    }
}