package cloud.eka_dev.ftracker.ui.screen.home.component

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import cloud.eka_dev.ftracker.data.model.Transaction

@Composable
fun TransactionGroup(
    date: String,
    items: List<Transaction>,
    onEditClick: (Transaction) -> Unit,
    onDeleteClick: (Transaction) -> Unit
) {
    Text(
        date,
        fontWeight = FontWeight.Bold,
        fontSize = 18.sp,
        color = Color.White
    )

    items.forEach { transaction ->
        TransactionItem(
            transaction = transaction,
            onEditClick = onEditClick,
            onDeleteClick = onDeleteClick
        )
    }
}
