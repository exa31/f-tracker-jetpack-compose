package cloud.eka_dev.ftracker.ui.screen.edit_transaction

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import cloud.eka_dev.ftracker.data.enums.TransactionType
import cloud.eka_dev.ftracker.utils.formatDateForServer
import cloud.eka_dev.ftracker.utils.formatToRupiah
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditTransactionScreen(
    onSuccess: () -> Unit,
    onClose: () -> Unit,
    vm: EditTransactionViewModel = hiltViewModel()
) {
    val date by vm.date.collectAsState()
    var textFieldWidth by remember { mutableStateOf(0) }
    val description by vm.description.collectAsState()
    val type by vm.type.collectAsState()
    val amount by vm.amount.collectAsState()

    var descriptionError by remember { mutableStateOf("") }
    var typeError by remember { mutableStateOf("") }
    var amountError by remember { mutableStateOf("") }


    var isDescriptionFocused by remember { mutableStateOf(false) }
    var isAmountFocused by remember { mutableStateOf(false) }


    val transactionTypes = TransactionType.entries.map { it.label }
    var expanded by remember { mutableStateOf(false) }

    var showDatePicker by remember { mutableStateOf(false) }
    val datePickerState = rememberDatePickerState(
        initialSelectedDateMillis = System.currentTimeMillis()
    )

    val loading by vm.loading.collectAsState()

    val notFound by vm.notFound.collectAsState()

    val GreenPrimary = Color(0xFF4ADE80)

    fun validate(): Boolean {
        var valid = true

        // Description
        if (description.isBlank()) {
            descriptionError = "Description is required"
            valid = false
        } else {
            descriptionError = ""
        }

        // Type
        if (type.isBlank()) {
            typeError = "Please select a transaction type"
            valid = false
        } else {
            typeError = ""
        }

        // Amount
        val amt = amount.text.replace("[^\\d]".toRegex(), "").toIntOrNull() ?: 0
        if (amt <= 1000) {
            amountError = "Amount must be greater than Rp 1,000"
            valid = false
        } else {
            amountError = ""
        }

        return valid
    }

    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(Unit) {
        vm.snackbarMessage.collect { msg ->
            snackbarHostState.showSnackbar(msg)
        }
    }


    if (showDatePicker) {
        DatePickerDialog(
            onDismissRequest = { showDatePicker = false },
            confirmButton = {
                TextButton(
                    onClick = {
                        val selectedMillis = datePickerState.selectedDateMillis
                        if (selectedMillis != null) {
                            vm.onDateChange(
                                SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
                                    .format(Date(selectedMillis))
                            )
                        }
                        showDatePicker = false
                    }
                ) {
                    Text("OK")
                }
            },
            dismissButton = {
                TextButton(onClick = { showDatePicker = false }) {
                    Text("Cancel")
                }
            }
        ) {
            DatePicker(state = datePickerState)
        }
    }

    if (loading) {

        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFF000000)),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator(
                strokeWidth = 2.dp,
                modifier = Modifier.size(50.dp),
                color = Color.White
            )
        }
    } else if (notFound) {

        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFF121212)) // background hitam gelap
                .padding(24.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Box(
                modifier = Modifier
                    .background(Color(0xFF1E1E1E), shape = RoundedCornerShape(12.dp)) // card gelap
                    .border(1.dp, Color(0xFF2A2A2A), RoundedCornerShape(12.dp))
                    .padding(24.dp)
            ) {

                Column(
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {

                    Text(
                        "Transaction not found",
                        color = Color.White,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold
                    )

                    Spacer(Modifier.height(8.dp))

                    Text(
                        "The requested transaction was not found. It may have been deleted or is unavailable.",
                        color = Color(0xFFBBBBBB),
                        fontSize = 14.sp,
                        textAlign = TextAlign.Center
                    )

                    Spacer(Modifier.height(24.dp))

                    Button(
                        onClick = { onClose() },
                        colors = ButtonDefaults.buttonColors(containerColor = GreenPrimary)
                    ) {
                        Text("Back", color = Color.White)
                    }
                }
            }
        }
        return
    } else Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        text = "Form Transaction",
                        color = Color.White,
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp
                    )
                },
                navigationIcon = {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Back",
                        tint = Color.White,
                        modifier = Modifier.clickable { onClose() }
                    )
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = Color(0xFF1B1B1F)
                )
            )
        },
        containerColor = Color(0xFF1B1B1F)
    ) { padding ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(24.dp)
        ) {

            Spacer(Modifier.height(8.dp))

            // Date Field with Border (Looks like OutlinedTextField)
            Column {
                Text(
                    "Date",
                    color = Color.Gray,
                    fontSize = 12.sp,
                    modifier = Modifier.padding(start = 4.dp, bottom = 4.dp)
                )

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp)
                        .clickable { showDatePicker = true }
                        .border(
                            width = 1.dp,
                            color = if (showDatePicker) GreenPrimary else Color.Gray,
                        )
                        .padding(horizontal = 12.dp),
                    contentAlignment = Alignment.CenterStart
                ) {

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = date,
                            color = Color.White,
                            fontSize = 16.sp,
                            modifier = Modifier.weight(1f)
                        )

                        Icon(
                            imageVector = Icons.Default.DateRange,
                            contentDescription = null,
                            tint = Color.White
                        )
                    }
                }
            }



            Spacer(Modifier.height(12.dp))

            // Description
            Column {
                Text(
                    "Description",
                    color = Color.Gray,
                    fontSize = 12.sp,
                    modifier = Modifier.padding(start = 4.dp, bottom = 4.dp)
                )

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp)
                        .border(
                            width = 1.dp,
                            color = when {
                                isDescriptionFocused -> GreenPrimary
                                descriptionError.isNotEmpty() -> Color.Red
                                else -> Color.Gray
                            }
                        ),
                    contentAlignment = Alignment.CenterStart
                ) {
                    TextField(
                        value = description,
                        onValueChange = { vm.onDescriptionChange(it) },
                        placeholder = { Text("Enter description", color = Color.Gray) },
                        singleLine = true,
                        colors = TextFieldDefaults.colors(
                            focusedContainerColor = Color.Transparent,
                            unfocusedContainerColor = Color.Transparent,
                            focusedIndicatorColor = Color.Transparent,
                            unfocusedIndicatorColor = Color.Transparent,
                            cursorColor = Color(0xFF4ADE80),
                            focusedTextColor = Color.White,
                            unfocusedTextColor = Color.White,
                        ),
                        modifier = Modifier
                            .fillMaxWidth()
                            .onFocusChanged(
                                {
                                    isDescriptionFocused = it.isFocused
                                })
                    )
                }
            }
            if (descriptionError.isNotEmpty()) {
                Text(descriptionError, color = Color.Red, fontSize = 12.sp)
            }

            Spacer(Modifier.height(12.dp))

            // Type Field (Looks like OutlinedTextField)
            Column {
                Text(
                    "Type",
                    color = Color.Gray,
                    fontSize = 12.sp,
                    modifier = Modifier.padding(start = 4.dp, bottom = 4.dp)
                )

                Box {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(56.dp)
                            .border(
                                width = 1.dp,
                                color = when {
                                    expanded -> GreenPrimary
                                    typeError.isNotEmpty() -> Color.Red
                                    else -> Color.Gray
                                }
                            )
                            .onGloballyPositioned { coordinates ->
                                textFieldWidth = coordinates.size.width
                            }
                            .padding(horizontal = 12.dp)
                            .clickable { expanded = true },
                        contentAlignment = Alignment.CenterStart
                    )
                    {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = type.ifEmpty { "Select type" },
                                color = if (type.isEmpty()) Color.Gray else Color.White,
                                fontSize = 16.sp,
                                modifier = Modifier.weight(1f)
                            )

                            Icon(
                                Icons.Default.ArrowDropDown,
                                contentDescription = null,
                                tint = Color.White
                            )
                        }
                    }

                    DropdownMenu(
                        expanded = expanded,
                        onDismissRequest = { expanded = false },

                        modifier = Modifier
                            .background(Color(0xFF1B1B1F))
                            .width(with(LocalDensity.current) { textFieldWidth.toDp() })
                    ) {
                        transactionTypes.forEach { t ->
                            DropdownMenuItem(
                                text = { Text(t, color = Color.White) },
                                onClick = {
                                    vm.onTypeChange(t)
                                    expanded = false
                                }
                            )
                        }
                    }
                }
            }
            if (typeError.isNotEmpty()) {
                Text(typeError, color = Color.Red, fontSize = 12.sp)
            }


            Spacer(Modifier.height(12.dp))

            // Amount
            Column {
                Text(
                    "Amount",
                    color = Color.Gray,
                    fontSize = 12.sp,
                    modifier = Modifier.padding(start = 4.dp, bottom = 4.dp)
                )

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp)
                        .border(
                            width = 1.dp,
                            color = when {
                                isAmountFocused -> GreenPrimary
                                amountError.isNotEmpty() -> Color.Red
                                else -> Color.Gray
                            }
                        ),
                    contentAlignment = Alignment.CenterStart
                ) {
                    TextField(
                        value = amount,
                        onValueChange = { newValue ->
                            // Ambil angka mentah (tanpa Rp dan titik)
                            val clean = newValue.text.replace("[^\\d]".toRegex(), "")

                            // Format ke rupiah
                            val formatted = if (clean.isNotEmpty()) {
                                formatToRupiah(clean)
                            } else {
                                ""
                            }

                            // Update state dengan cursor di paling akhir
                            vm.onAmountChange(
                                amount.copy(
                                    text = formatted,
                                    selection = TextRange(formatted.length) // cursor ke ujung
                                )
                            )
                        },
                        placeholder = { Text("Enter amount", color = Color.Gray) },
                        singleLine = true,
                        keyboardOptions = KeyboardOptions.Default.copy(
                            imeAction = ImeAction.Done
                        ),
                        keyboardActions = KeyboardActions(
                            onDone = {
                                // Close keyboard
                                if (!validate()) return@KeyboardActions
                                val amt = amount.text.replace("[^\\d]".toRegex(), "").toInt()

                                vm.updateTransaction(
                                    formatDateForServer(date),
                                    description,
                                    TransactionType.valueOf(type.uppercase()),
                                    amt,
                                    onSuccess = onSuccess
                                )
                            }
                        ),
                        colors = TextFieldDefaults.colors(
                            focusedContainerColor = Color.Transparent,
                            unfocusedContainerColor = Color.Transparent,
                            focusedIndicatorColor = Color.Transparent,
                            unfocusedIndicatorColor = Color.Transparent,
                            cursorColor = Color(0xFF4ADE80),
                            focusedTextColor = Color.White,
                            unfocusedTextColor = Color.White,
                        ),
                        modifier = Modifier
                            .fillMaxWidth()
                            .onFocusChanged(
                                {
                                    isAmountFocused = it.isFocused
                                }
                            )
                    )
                }
            }

            if (amountError.isNotEmpty()) {
                Text(amountError, color = Color.Red, fontSize = 12.sp)
            }


            Spacer(Modifier.height(24.dp))

            Button(
                onClick = {

                    if (!validate()) return@Button

                    val amt = amount.text.replace("[^\\d]".toRegex(), "").toInt()

                    vm.updateTransaction(
                        formatDateForServer(date),
                        description,
                        TransactionType.valueOf(type.uppercase()),
                        amt,
                        onSuccess = onSuccess
                    )

                },
                enabled = !loading,
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(containerColor = GreenPrimary)
            ) {
                if (loading) {
                    CircularProgressIndicator(
                        strokeWidth = 2.dp,
                        modifier = Modifier.size(22.dp),
                        color = Color.White
                    )
                } else {
                    Text("Save", color = Color.White)
                }
            }
        }
    }
}
