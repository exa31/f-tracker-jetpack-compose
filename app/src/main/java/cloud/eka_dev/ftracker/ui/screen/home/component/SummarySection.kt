package cloud.eka_dev.ftracker.ui.screen.home.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
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
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cloud.eka_dev.ftracker.utils.formatCurrency

// App colors
private val PrimaryGreen = Color(0xFF4ADE80)
private val ExpenseRed = Color(0xFFEF4444)
private val CardBg = Color(0xFF1F1F22) // ⬅️ Lebih gelap dari background tapi tetap beda

@Composable
fun SummarySection(income: Long, expense: Long) {
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
            title = "Expanse",
            amount = expense,
            color = ExpenseRed,
            icon = Icons.AutoMirrored.Outlined.TrendingDown, // ⬅️ Expense turun ✔
            modifier = Modifier.weight(1f)
        )
    }
}

@Composable
fun SummaryCard(
    modifier: Modifier = Modifier,
    title: String,
    amount: Long,
    percentage: Int = 0,
    view: String = "month",
    color: Color,
    icon: ImageVector,
) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = CardBg),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {

            // Title
            Text(
                title,
                fontWeight = FontWeight.SemiBold,
                fontSize = 14.sp,
                color = Color.White
            )

            Spacer(Modifier.height(4.dp))

            // Amount
            Text(
                text = formatCurrency(amount),
                fontWeight = FontWeight.Bold,
                color = color,
                fontSize = 18.sp
            )

            Spacer(Modifier.height(6.dp))

            // Percentage + icon
            Row(verticalAlignment = Alignment.CenterVertically) {

                // Icon di kiri persentase
                Icon(
                    icon,
                    contentDescription = null,
                    tint = color,
                    modifier = Modifier.size(16.dp)
                )

                Spacer(Modifier.width(6.dp))

                Text(
                    "$percentage% vs last $view",
                    color = color,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Medium
                )
            }
        }
    }
}