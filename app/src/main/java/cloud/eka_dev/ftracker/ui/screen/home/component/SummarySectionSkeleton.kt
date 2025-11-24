package cloud.eka_dev.ftracker.ui.screen.home.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
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
private val CardBg = Color(0xFF1F1F22)

@Composable
fun SummarySectionSkeleton() {
    Row(
        horizontalArrangement = Arrangement.spacedBy(10.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        SummaryCardSkeleton(modifier = Modifier.weight(1f))
        SummaryCardSkeleton(modifier = Modifier.weight(1f))
    }
}

@Composable
fun SummaryCardSkeleton(modifier: Modifier = Modifier) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = CardBg),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            // Title skeleton
            Box(
                modifier = Modifier
                    .fillMaxWidth(0.5f)
                    .height(14.dp)
                    .background(SkeletonGray, RoundedCornerShape(4.dp))
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Amount skeleton
            Box(
                modifier = Modifier
                    .fillMaxWidth(0.7f)
                    .height(18.dp)
                    .background(SkeletonGray, RoundedCornerShape(4.dp))
            )

            Spacer(modifier = Modifier.height(12.dp))

            // Percentage + icon skeleton
            Row {
                Box(
                    modifier = Modifier
                        .size(16.dp)
                        .background(SkeletonGray, RoundedCornerShape(4.dp))
                )

                Spacer(modifier = Modifier.width(6.dp))

                Box(
                    modifier = Modifier
                        .fillMaxWidth(0.3f)
                        .height(13.dp)
                        .background(SkeletonGray, RoundedCornerShape(4.dp))
                )
            }
        }
    }
}
