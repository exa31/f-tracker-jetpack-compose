package com.example.f_tracker_kotlin.ui.screen.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Logout
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.f_tracker_kotlin.data.model.Transaction
import com.example.f_tracker_kotlin.ui.component.SummarySection
import com.example.f_tracker_kotlin.ui.component.TransactionGroup

@Composable
fun HomeScreen(
    transactions: Map<String, List<Transaction>>,
    income: Long,
    expense: Long,
    onAddClick: () -> Unit,
    onEditClick: (Transaction) -> Unit,
    onDeleteClick: (Transaction) -> Unit,
    onLogoutSuccess: () -> Unit,
    vm: HomeViewModel = hiltViewModel()
) {
    Scaffold(
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
            Surface(
                shadowElevation = 6.dp,
                color = Color.Black // ⬅ Hitam
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 14.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "FTracker",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold,
                        color = Color.White // ⬅ Putih
                    )
                    IconButton(
                        onClick = {
                            vm.logOut(onSuccess = onLogoutSuccess)
                        }
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.Logout, // Ganti dengan ikon profil yang sesuai
                            contentDescription = "Logout",
                            tint = Color.White // ⬅ Putih,
                        )
                    }
                }
            }
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(16.dp)
        ) {

            SummarySection(income, expense)

            Spacer(Modifier.height(24.dp))

            Text(
                "Transactions",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                color = Color.White // ⬅ Putih
            )

            Spacer(Modifier.height(12.dp))

            transactions.forEach { (date, items) ->
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
