package org.classapp.expensetrackerapp

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowLeft
import androidx.compose.material3.ButtonDefaults
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
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import org.classapp.expensetrackerapp.data.deleteTransactionData
import org.classapp.expensetrackerapp.data.getTransactionDataById
import org.classapp.expensetrackerapp.data.TransactionData
import org.classapp.expensetrackerapp.data.updateTransactionData
import org.classapp.expensetrackerapp.data.currentDateFromDatePicker
import org.classapp.expensetrackerapp.data.currentInspectTransactionDataId
import org.classapp.expensetrackerapp.data.currentInspectTransactionDataType
import org.classapp.expensetrackerapp.ui.theme.ExpenseTrackerAppTheme

@Composable
fun EditIncomeDataScreen(navController: NavController) {
    ExpenseTrackerAppTheme (dynamicColor = false) {
        Surface (
            modifier = Modifier
                .fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        )
        {
            EditIncomeTopAppBar(navController)
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditIncomeTopAppBar(navController: NavController)
{
    val currentInspectTransaction = getTransactionDataById(currentInspectTransactionDataType, currentInspectTransactionDataId);
    var noteValue by remember { mutableStateOf(TextFieldValue(currentInspectTransaction.note.toString())) }
    var amountValue by remember { mutableStateOf(TextFieldValue(currentInspectTransaction.amount.toString())) }
    val dateValue by remember { mutableStateOf(currentInspectTransaction.date) }
    val context = LocalContext.current

    Scaffold (
        topBar = {
            TopAppBar(
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = colorResource(id = R.color.light_green),
                    titleContentColor = Color.Black,
                ),
                title = { Text(text = "Edit Income")
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
                    },
                    label = { Text("Amount") },
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Number
                    ),
                    modifier = Modifier
                        .padding(bottom = 8.dp)
                        .fillMaxWidth()
                )

                ContentDatePickerPopup(dateValue.toString().substring(0, 10))

                Column (
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(top = 30.dp),
                    verticalArrangement = Arrangement.SpaceBetween,
                    horizontalAlignment = Alignment.End
                ) {
                    Row {
                        OutlinedButton(
                            onClick = {
                                deleteTransactionData(context, currentInspectTransactionDataType, currentInspectTransactionDataId)
                                navController.popBackStack()
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = colorResource(id = R.color.pearl)),
                            border = BorderStroke(1.dp, colorResource(id = R.color.light_green)),
                            modifier = Modifier
                                .size(width = 170.dp, height = 50.dp)
                                .padding(end = 15.dp)
                        ) {
                            Text(
                                text = "Delete",
                                style = TextStyle(color = colorResource(id = R.color.light_green))
                            )
                        }

                        FilledTonalButton(
                            onClick = {
                                updateTransactionData(context, currentInspectTransactionDataType,
                                    TransactionData(
                                        id = currentInspectTransactionDataId,
                                        note = noteValue.text,
                                        isIncome = true,
                                        amount = amountValue.text.toDouble(),
                                        date = currentDateFromDatePicker))
                                navController.popBackStack()
                            },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = colorResource(id = R.color.light_green)
                            ),
                            modifier = Modifier
                                .size(width = 170.dp, height = 50.dp)
                                .padding(start = 15.dp)
                        ) {
                            Text("Confirm")
                        }
                    }
                }
            }
        }
    }
}

@Preview
@Composable
fun EditIncomeDataScreenPreview() {
    EditIncomeDataScreen(navController = rememberNavController())
}