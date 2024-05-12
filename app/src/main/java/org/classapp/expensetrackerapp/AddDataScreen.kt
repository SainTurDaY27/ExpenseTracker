package org.classapp.expensetrackerapp

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import org.classapp.expensetrackerapp.data.formatNumberWithCommas
import org.classapp.expensetrackerapp.data.getLatestTransactionData
import org.classapp.expensetrackerapp.data.TransactionData
import org.classapp.expensetrackerapp.data.TransactionType
import org.classapp.expensetrackerapp.data.currentInspectTransactionDataId
import org.classapp.expensetrackerapp.data.currentInspectTransactionDataType
import org.classapp.expensetrackerapp.ui.theme.ExpenseTrackerAppTheme

@Composable
fun AddDataScreen(navController: NavController) {

    val latestTransactionData = getLatestTransactionData(5)

    ExpenseTrackerAppTheme (dynamicColor = false) {
        Surface(
            modifier = Modifier
                .fillMaxSize(),
                //.padding(16.dp),
            color = MaterialTheme.colorScheme.background
        ) {
            Column (
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.Top,
                horizontalAlignment = Alignment.Start
            )
            {
                Text(
                    text = "Latest 5 items",
                    textAlign = TextAlign.Left,
                    fontWeight = FontWeight.Bold,
                    fontSize = 24.sp,
                    modifier = Modifier.padding(start = 20.dp, top = 20.dp, end = 8.dp),
                )
            }
            Column (
                modifier = Modifier
                    .fillMaxSize()
                    .padding(top = 56.dp, start = 12.dp, end = 12.dp)
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.Top,
                horizontalAlignment = Alignment.CenterHorizontally) {

                for (transactionData in latestTransactionData) {
                    TransactionDisplayCard(navController, transactionData)
                }

                Row (
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(top = 20.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Button(
                        onClick = {
                            navController.navigate(DestinationScreens.AddIncome.route)
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = colorResource(id = R.color.light_green)),
                        modifier = Modifier
                            .padding(start = 10.dp)
                            .size(width = 150.dp, height = 50.dp)
                    ) {
                        Text(text = "Add Income")
                    }
                    OutlinedButton(
                        onClick = {
                            navController.navigate(DestinationScreens.AddExpense.route)
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = colorResource(id = R.color.pearl)),
                        border = BorderStroke(1.dp, colorResource(id = R.color.light_green)),
                        modifier = Modifier
                            .padding(end = 10.dp)
                            .size(width = 150.dp, height = 50.dp)
                    ) {
                        Text(text = "Add Expense", style = TextStyle(color = colorResource(id = R.color.black)))
                    }
                }
            }
        }
    }
}

@Composable
fun TransactionDisplayCard(navController: NavController, transactionData: TransactionData) {
    val isIncome = transactionData.isIncome
    val image = if (isIncome) {
        painterResource(id = R.drawable.ic_income)
    } else {
        painterResource(id = R.drawable.ic_other_expense)
    }

    val color = if (isIncome) {
        colorResource(id = R.color.green)
    } else {
        colorResource(id = R.color.red)
    }

    val route = if (isIncome) {
        DestinationScreens.EditIncome.route
    } else {
        DestinationScreens.EditExpense.route
    }

    Card( elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        modifier = Modifier
            .padding(6.dp)
            .fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = colorResource(id = R.color.yellow_white),
        )
    ) {
        Row (verticalAlignment = Alignment.CenterVertically) {
            Image(painter = image,
                contentDescription = "displayImage",
                modifier = Modifier
                    .padding(8.dp)
                    .size(50.dp))
            Column (
                modifier = Modifier
                    .padding(8.dp)
                    .weight(1f)
            ) {
                Text(
                    text = transactionData.note!!,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Left,
                    modifier = Modifier.padding(start = 8.dp),
                    style = TextStyle(fontSize = 20.sp)
                )
                Text(
                    text = "฿" + formatNumberWithCommas(transactionData.amount),
                    color = color,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.End,
                    modifier = Modifier.padding(start = 8.dp, end = 8.dp),
                    style = TextStyle(fontSize = 22.sp)
                )
                Text(
                    text = transactionData.date.toString().substring(0, 10),
                    textAlign = TextAlign.Left,
                    modifier = Modifier.padding(start = 10.dp, top = 4.dp),
                    style = TextStyle(fontSize = 12.sp)
                )
            }
            Column (
                modifier = Modifier.fillMaxHeight(),
                horizontalAlignment = Alignment.End,
                verticalArrangement = Arrangement.Top,
            ){
                IconButton(onClick = {
                    navController.navigate(route)
                    currentInspectTransactionDataId = transactionData.id!!
                    currentInspectTransactionDataType = if (isIncome) {
                        TransactionType.INCOME
                    } else {
                        TransactionType.EXPENSE
                    }
                }) {
                    Icon(
                        Icons.Filled.Edit,
                        contentDescription = "Edit"
                    )
                }
            }
        }
    }
}

@Preview
@Composable
fun AddDataScreenPreview() {
    AddDataScreen(navController = rememberNavController())
}