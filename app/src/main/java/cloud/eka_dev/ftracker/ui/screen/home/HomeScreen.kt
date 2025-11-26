package cloud.eka_dev.ftracker.ui.screen.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Logout
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import cloud.eka_dev.ftracker.data.model.Transaction
import cloud.eka_dev.ftracker.ui.screen.home.component.SummarySection
import cloud.eka_dev.ftracker.ui.screen.home.component.SummarySectionSkeleton
import cloud.eka_dev.ftracker.ui.screen.home.component.TransactionFilterTabs
import cloud.eka_dev.ftracker.ui.screen.home.component.TransactionGroup
import cloud.eka_dev.ftracker.ui.screen.home.component.TransactionSkeleton
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    onAddClick: () -> Unit,
    onEditClick: (Transaction) -> Unit,
    onLogoutSuccess: () -> Unit,
    vm: HomeViewModel = hiltViewModel()
) {

    val income by vm.income.collectAsState()
    val expanse by vm.expanse.collectAsState()
    val percentageIncome by vm.percentageIncome.collectAsState()
    val percentageExpanse by vm.percentageExpanse.collectAsState()
    val isUpTrandIncome by vm.isUpTrandIncome.collectAsState()
    val isUpTrandExpanse by vm.isUpTrandExpanse.collectAsState()
    val transactionsByDate by vm.dataByDate.collectAsState()
    val loading by vm.loading.collectAsState()
    val loadingProggres by vm.loadingProggress.collectAsState()
    val isRefreshing by vm.isRefreshing.collectAsState()
    val selectedView by vm.selectedView.collectAsState()

    var showConfirmDialog by remember { mutableStateOf(false) }


    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(Unit) {
        vm.snackbarMessage.collect { msg ->
            snackbarHostState.showSnackbar(msg)
        }
    }

    if (showConfirmDialog) {
        AlertDialog(
            onDismissRequest = { showConfirmDialog = false },
            title = {
                Text(
                    text = "Delete Transaction",
                    style = MaterialTheme.typography.titleLarge
                )
            },
            text = {
                Text(
                    text = "Are you sure you want to delete this transaction? This action cannot be undone.",
                    style = MaterialTheme.typography.bodyMedium
                )
            },
            confirmButton = {
                Button(
                    onClick = {
                        showConfirmDialog = false
                        vm.deleteTransaction()
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color.Red
                    ),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text("Delete")
                }
            },
            dismissButton = {
                OutlinedButton(
                    onClick = { showConfirmDialog = false },
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text("Cancel")
                }
            }
        )
    }


    SwipeRefresh(
        state = rememberSwipeRefreshState(isRefreshing = isRefreshing),
        onRefresh = {
            vm.handleRefresh()
        },
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

                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(
                            top = 16.dp,
                            start = 16.dp,
                            end = 16.dp,
                        )
                ) {

                    item {
                        if (loading) {
                            SummarySectionSkeleton()
                        } else {
                            SummarySection(
                                income = income,
                                expense = expanse,
                                percentageIncome = percentageIncome,
                                percentageExpanse = percentageExpanse,
                                isUpTrandIncome = isUpTrandIncome,
                                isUpTrandExpanse = isUpTrandExpanse
                            )
                        }

                        Spacer(Modifier.height(24.dp))

                        Text(
                            "Transactions",
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )

                        Spacer(Modifier.height(12.dp))

                        TransactionFilterTabs(
                            selected = selectedView,
                            onSelect = { vm.onViewOptionChange(it) }
                        )

                        Spacer(Modifier.height(12.dp))
                    }

                    if (loading) {
                        items(3) {
                            TransactionSkeleton()
                            Spacer(Modifier.height(12.dp))
                        }
                    } else {

                        // group per tanggal
                        transactionsByDate.forEach { (date, items) ->

                            item {
                                TransactionGroup(
                                    date = date,
                                    items = items,
                                    onEditClick = onEditClick,
                                    onDeleteClick = {
                                        if (loadingProggres) return@TransactionGroup
                                        vm.onDeleteClick(it)
                                        showConfirmDialog = true
                                    }
                                )

                                Spacer(Modifier.height(12.dp))
                            }
                        }
                    }
                    item { Spacer(Modifier.height(80.dp)) }
                }

            }
        }
    }
}
