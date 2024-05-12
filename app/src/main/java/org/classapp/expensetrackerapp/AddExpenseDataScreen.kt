package org.classapp.expensetrackerapp

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowLeft
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import org.classapp.expensetrackerapp.data.addTransactionData
import org.classapp.expensetrackerapp.data.ExpenseType
import org.classapp.expensetrackerapp.data.getLatestTransactionDataId
import org.classapp.expensetrackerapp.data.TransactionData
import org.classapp.expensetrackerapp.data.TransactionType
import org.classapp.expensetrackerapp.data.currentDateFromDatePicker
import org.classapp.expensetrackerapp.data.currentPickedExpenseType
import org.classapp.expensetrackerapp.ui.theme.ExpenseTrackerAppTheme

@Composable
fun AddExpenseDataScreen(navController: NavController) {
    ExpenseTrackerAppTheme (dynamicColor = false) {
        Surface (
            modifier = Modifier
                .fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        )
        {
            ExpenseTopAppBar(navController)
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ExpenseTopAppBar(navController: NavController)
{
    var noteValue by remember { mutableStateOf(TextFieldValue()) }
    var amountValue by remember { mutableStateOf(TextFieldValue()) }
    val context = LocalContext.current
    var isAmountFilled: Boolean = false

    Scaffold (
        topBar = {
            TopAppBar(
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = colorResource(id = R.color.light_green),
                    titleContentColor = Color.Black,
                ),
                title = { Text(text = "Add Expense")
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

                ContentDatePickerPopup()

                Text (
                    text = "Choose expense type",
                    fontSize = 15.sp,
                    modifier = Modifier.padding(top = 18.dp, bottom = 10.dp)
                )
                ExpenseTypeDropdown()

                Column (
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(top = 30.dp),
                    verticalArrangement = Arrangement.SpaceBetween,
                    horizontalAlignment = Alignment.End
                ) {
                    FilledTonalButton(
                        onClick = {
                            addTransactionData(context, TransactionType.EXPENSE,
                                    TransactionData(
                                        id = (getLatestTransactionDataId(TransactionType.EXPENSE) + 1).toString(),
                                        note = noteValue.text,
                                        isIncome = false,
                                        amount = amountValue.text.toDouble(),
                                        expenseType = currentPickedExpenseType,
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ExpenseTypeDropdown()
{
    val context = LocalContext.current
    val expenseType = listOf(ExpenseType.HOUSING, ExpenseType.TRANSPORTATION, ExpenseType.FOOD, ExpenseType.CLOTHING, ExpenseType.HEALTHCARE, ExpenseType.OTHER)
    var expanded by remember { mutableStateOf(false) }
    var selectedExpenseType by remember { mutableStateOf(expenseType[0].toString()) }

    Box (
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 8.dp)
            .height(55.dp)
            .background(MaterialTheme.colorScheme.surface)
            .border(1.dp, Color.Black, MaterialTheme.shapes.extraSmall)
    ) {
        ExposedDropdownMenuBox(
            expanded = expanded,
            onExpandedChange = {
                expanded = !expanded
            }
        ) {
            TextField(
                value = selectedExpenseType,
                onValueChange = {},
                readOnly = true,
                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
                modifier = Modifier
                    .menuAnchor()
                    .fillMaxSize(),
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = colorResource(id = R.color.dark_pearl),
                    unfocusedContainerColor = colorResource(id = R.color.dark_pearl),
                )
            )

            ExposedDropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false },
            ) {
                expenseType.forEach { item ->
                    DropdownMenuItem(
                        text = { Text(text = item.toString()) },
                        onClick = {
                            selectedExpenseType = item.toString()
                            currentPickedExpenseType = item
                            expanded = false
                        })
                }
            }
        }
    }
}

@Preview
@Composable
fun AddExpenseDataScreenPreview() {
    AddExpenseDataScreen(navController = rememberNavController())
}