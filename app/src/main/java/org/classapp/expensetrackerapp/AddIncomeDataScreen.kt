package org.classapp.expensetrackerapp

import android.annotation.SuppressLint
import android.icu.text.SimpleDateFormat
import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowLeft
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.DisplayMode
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults.topAppBarColors
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import org.classapp.expensetrackerapp.data.addTransactionData
import org.classapp.expensetrackerapp.data.getLatestTransactionDataId
import org.classapp.expensetrackerapp.data.TransactionData
import org.classapp.expensetrackerapp.data.TransactionType
import org.classapp.expensetrackerapp.data.currentDateFromDatePicker
import org.classapp.expensetrackerapp.ui.theme.ExpenseTrackerAppTheme
import java.sql.Date
import java.sql.Timestamp

@Composable
fun AddIncomeDataScreen(navController: NavController) {
    ExpenseTrackerAppTheme {
        Surface (
            modifier = Modifier
                .fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        )
        {
            IncomeTopAppBar(navController)
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun IncomeTopAppBar(navController: NavController)
{
    var noteValue by remember { mutableStateOf(TextFieldValue()) }
    var amountValue by remember { mutableStateOf(TextFieldValue()) }
    val context = LocalContext.current
    var isAmountFilled: Boolean = false

    Scaffold (
        topBar = {
            TopAppBar(
                colors = topAppBarColors(
                    containerColor = colorResource(id = R.color.light_green),
                    titleContentColor = Color.Black,
                ),
                title = { Text(text = "Add Income")
                },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            imageVector = Icons.Filled.KeyboardArrowLeft,
                            contentDescription = "Back"
                        )
                    }
                }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize(),
        ) {
            Column (
                modifier = Modifier
                    .fillMaxSize()
                    .padding(20.dp),
                verticalArrangement = Arrangement.Top,
                horizontalAlignment = Alignment.Start
            )
            {
                Text(
                    text = "Transaction Data",
                    fontWeight = FontWeight.Bold,
                    fontSize = 20.sp,
                    modifier = Modifier.padding(bottom = 8.dp)
                )
                OutlinedTextField(
                    value = noteValue,
                    onValueChange = {
                        noteValue = it
                    },
                    label = { Text("Note") },
                    modifier = Modifier
                        .padding(bottom = 8.dp)
                        .fillMaxWidth()
                )
                OutlinedTextField(
                    value = amountValue,
                    onValueChange = {
                        amountValue = it
                        isAmountFilled = amountValue.text.isNotEmpty()
                    },
                    label = { Text("Amount") },
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Number
                    ),
                    modifier = Modifier
                        .padding(bottom = 8.dp)
                        .fillMaxWidth()
                )

                ContentDatePickerPopup()

                Column (
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(top = 30.dp),
                    verticalArrangement = Arrangement.SpaceBetween,
                    horizontalAlignment = Alignment.End
                ) {
                    FilledTonalButton(
                        onClick = {
                            // TODO: Add data to json later
                            addTransactionData(context, TransactionType.INCOME,
                                TransactionData(
                                    id = (getLatestTransactionDataId(TransactionType.INCOME) + 1).toString(),
                                    note = noteValue.text,
                                    isIncome = true,
                                    amount = amountValue.text.toDouble(),
                                    date = currentDateFromDatePicker
                                )
                            )
                            navController.popBackStack()
                        },
                        enabled = isAmountFilled,
                        colors = ButtonDefaults.buttonColors(
                            containerColor = colorResource(id = R.color.light_green)
                        ),
                        modifier = Modifier
                            .size(width = 150.dp, height = 50.dp)
                        ) {
                        Text("Add")
                    }
                }
            }
        }
    }
}

@SuppressLint("SimpleDateFormat")
fun convertLongToTime(time: Long): String {
    val date = Date(time)
    val format = SimpleDateFormat("yyyy.MM.dd")
    return format.format(date)
}

@SuppressLint("UnrememberedMutableState")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ContentDatePickerPopup(previousDate: String = "") {
    val dateStringValue = when (previousDate) {
        "" -> "Date Picker"
        else -> "Date Picker: $previousDate"
    }
    var dateResult by remember { mutableStateOf(dateStringValue) }
    val openDialog = remember { mutableStateOf(false) }
    //var isDateSelected: Boolean = false

    Column (
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        OutlinedButton(
            onClick = { openDialog.value = true },
            modifier = Modifier
                .fillMaxWidth()
                .height(55.dp)
                .padding(top = 2.dp),
            shape = RoundedCornerShape(5.dp)
        ) {
            Text(
                dateResult,
                textAlign = TextAlign.Left,
                color = Color.Black,
            )
        }
    }
    if (openDialog.value) {
        val datePickerState = rememberDatePickerState()
        val state = rememberDatePickerState(initialDisplayMode = DisplayMode.Input)
        val confirmEnabled = derivedStateOf { datePickerState.displayMode != null }
        DatePickerDialog(
            onDismissRequest = {
                openDialog.value = false
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        openDialog.value = false
                        var date = "No selection"
                        date = if (datePickerState.selectedDateMillis != null){
                            convertLongToTime(datePickerState.selectedDateMillis!!)
                        } else {
                            convertLongToTime(System.currentTimeMillis())
                        }
                        currentDateFromDatePicker = if (datePickerState.selectedDateMillis != null){
                            Timestamp(datePickerState.selectedDateMillis!!)
                        } else {
                            Timestamp(System.currentTimeMillis())
                        }
                        dateResult = "Date Picker: $date"
                        Log.d("DatePicker", "Selected date: $date")
                    },
                    enabled = confirmEnabled.value
                ) {
                    Text(text = "Confirm")
                }
            },
            dismissButton = {
                TextButton(
                    onClick = {
                        openDialog.value = false
                    }
                ) {
                    Text(text = "Cancel")
                }
            }
        ) {
            DatePicker(state = datePickerState)
        }
    }
}

@Preview
@Composable
fun AddIncomeDataScreenPreview() {
    AddIncomeDataScreen(navController = rememberNavController())
}