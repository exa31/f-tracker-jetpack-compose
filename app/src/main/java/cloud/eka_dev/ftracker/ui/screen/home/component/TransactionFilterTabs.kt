package cloud.eka_dev.ftracker.ui.screen.home.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ScrollableTabRow
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import cloud.eka_dev.ftracker.data.enums.ViewOptions

private val PrimaryGreen = Color(0xFF4ADE80)

@Composable
fun TransactionFilterTabs(
    selected: ViewOptions,
    onSelect: (ViewOptions) -> Unit
) {
    val tabs = ViewOptions.entries.toList()

    ScrollableTabRow(
        selectedTabIndex = tabs.indexOf(selected),
        containerColor = Color.Black,
        contentColor = Color.White,
        edgePadding = 0.dp,
        divider = {},
        indicator = { tabPositions ->
            TabRowDefaults.SecondaryIndicator(
                modifier = Modifier
                    .tabIndicatorOffset(tabPositions[tabs.indexOf(selected)]),
                color = PrimaryGreen       // ⬅️ DI SINI GANTI WARNA
            )
        }
    ) {
        tabs.forEach { type ->
            val isSelected = type == selected

            Tab(
                selected = isSelected,
                onClick = { onSelect(type) },
                selectedContentColor = Color.Black,
                unselectedContentColor = Color.Gray,
            ) {
                Box(
                    modifier = Modifier
                        .padding(horizontal = 8.dp, vertical = 10.dp)
                        .background(
                            if (isSelected) Color.White else Color(0xFF1C1C1C),
                            shape = RoundedCornerShape(50)
                        )
                        .padding(horizontal = 16.dp, vertical = 8.dp)
                ) {
                    Text(
                        text = type.name.lowercase().replaceFirstChar { it.uppercase() },
                        color = if (isSelected) Color.Black else Color.Gray
                    )
                }
            }
        }
    }
}
