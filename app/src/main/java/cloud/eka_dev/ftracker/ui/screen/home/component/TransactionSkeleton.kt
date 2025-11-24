package cloud.eka_dev.ftracker.ui.screen.home.component

import androidx.compose.foundation.background
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
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

private val SkeletonGray = Color(0xFF2C2C2E)
private val CardBg = Color(0xFF1E1E1E)

@Composable
fun TransactionSkeleton(count: Int = 3) {
    // Skeleton tanggal
    Spacer(modifier = Modifier.height(8.dp))
    SkeletonText(widthFraction = 0.3f, height = 18)
    Spacer(modifier = Modifier.height(8.dp))

    repeat(count) {
        TransactionItemSkeleton()
        Spacer(modifier = Modifier.height(12.dp))
    }
}

@Composable
fun TransactionItemSkeleton() {
    Card(
        modifier = Modifier
            .fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = CardBg),
        elevation = CardDefaults.cardElevation(2.dp)
    ) {
        Row(
            Modifier
                .padding(12.dp)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            // Info kiri
            Column {
                SkeletonText(widthFraction = 0.5f, height = 14)
                Spacer(modifier = Modifier.height(6.dp))
                SkeletonText(widthFraction = 0.3f, height = 16)
            }
            // Tombol kanan (edit/delete)
            Row {
                SkeletonBox(size = 24)
                Spacer(modifier = Modifier.width(8.dp))
                SkeletonBox(size = 24)
            }
        }
    }
}

@Composable
fun SkeletonText(widthFraction: Float = 1f, height: Int = 14) {
    Spacer(
        modifier = Modifier
            .fillMaxWidth(widthFraction)
            .height(height.dp)
            .background(SkeletonGray, RoundedCornerShape(4.dp))
    )
}

@Composable
fun SkeletonBox(size: Int = 24) {
    Spacer(
        modifier = Modifier
            .size(size.dp)
            .background(SkeletonGray, RoundedCornerShape(4.dp))
    )
}
