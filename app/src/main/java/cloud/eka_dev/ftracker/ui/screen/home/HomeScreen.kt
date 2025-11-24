package cloud.eka_dev.ftracker.ui.screen.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Logout
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import cloud.eka_dev.ftracker.data.model.Transaction
import cloud.eka_dev.ftracker.ui.screen.home.component.SummarySection
import cloud.eka_dev.ftracker.ui.screen.home.component.SummarySectionSkeleton
import cloud.eka_dev.ftracker.ui.screen.home.component.TransactionGroup
import cloud.eka_dev.ftracker.ui.screen.home.component.TransactionSkeleton
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    onAddClick: () -> Unit,
    onEditClick: (Transaction) -> Unit,
    onDeleteClick: (Transaction) -> Unit,
    onLogoutSuccess: () -> Unit,
    vm: HomeViewModel = hiltViewModel()
) {

    val income by vm.income.collectAsState()
    val expanse by vm.expanse.collectAsState()
    val transactionsByDate by vm.dataByDate.collectAsState()
    val loading by vm.loading.collectAsState()

    // State buat pull-to-refresh
    val pullRefreshState = rememberPullToRefreshState()

    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(Unit) {
        vm.snackbarMessage.collect { msg ->
            snackbarHostState.showSnackbar(msg)
        }
    }

    SwipeRefresh(
        state = rememberSwipeRefreshState(loading),
        onRefresh = { vm.getTransactions() },
    ) {
        Scaffold(
            snackbarHost = {
                androidx.compose.material3.SnackbarHost(
                    hostState = snackbarHostState,
                    modifier = Modifier.padding(16.dp)
                )
            },
            modifier = Modifier.background(Color.Black),
            containerColor = Color.Black,
            floatingActionButton = {
                FloatingActionButton(
                    onClick = onAddClick,
                    containerColor = Color(0xFF4ADE80),
                    contentColor = Color.Black
                ) {
                    Icon(Icons.Default.Add, contentDescription = "Add")
                }
            },
            topBar = {
                CenterAlignedTopAppBar(
                    title = {
                        Text(
                            text = "FTracker",
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                    },
                    actions = {
                        IconButton(onClick = { vm.logOut(onSuccess = onLogoutSuccess) }) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.Logout,
                                contentDescription = "Logout",
                                tint = Color.White
                            )
                        }
                    },
                    colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                        containerColor = Color.Black
                    )
                )
            }
        ) { padding ->

            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)

            ) {

                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .verticalScroll(rememberScrollState())
                        .padding(
                            top = 16.dp,
                            start = 16.dp,
                            end = 16.dp,
                            bottom = 88.dp // ✨ kasih space biar FAB nggak nutup konten
                        )
                ) {

                    if (loading) {
                        SummarySectionSkeleton()
                    } else {
                        SummarySection(income, expanse)
                    }

                    Spacer(Modifier.height(24.dp))

                    Text(
                        "Transactions",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )

                    Spacer(Modifier.height(12.dp))

                    if (loading) {
                        // Tampilkan skeleton saat loading
                        repeat(3) {
                            TransactionSkeleton()
                            Spacer(Modifier.height(12.dp))
                        }
                    } else {

                        transactionsByDate.forEach { (date, items) ->
                            TransactionGroup(
                                date = date,
                                items = items,
                                onEditClick = onEditClick,
                                onDeleteClick = onDeleteClick,
                            )
                            Spacer(Modifier.height(12.dp))
                        }
                    }
                }

            }
        }
    }
}
