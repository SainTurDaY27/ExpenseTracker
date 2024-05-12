package org.classapp.expensetrackerapp

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import org.classapp.expensetrackerapp.data.ExpenseType
import org.classapp.expensetrackerapp.data.formatNumberWithCommas
import org.classapp.expensetrackerapp.data.getSumExpenseDataByType
import org.classapp.expensetrackerapp.data.getSumTransactionData
import org.classapp.expensetrackerapp.data.TransactionType
import org.classapp.expensetrackerapp.ui.theme.ExpenseTrackerAppTheme

@Composable
fun HomeScreen() {
    ExpenseTrackerAppTheme (dynamicColor = false) {
        Surface(
            modifier = Modifier
                .fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            Column (
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.Top,
                horizontalAlignment = Alignment.Start
            ) {
                TotalBalanceCard()
                Text(
                    text = "Expense Overview",
                    textAlign = TextAlign.Left,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(start = 16.dp, bottom = 8.dp)
                )
                Column (
                    modifier = Modifier
                        .fillMaxSize()
                        .verticalScroll(rememberScrollState())
                        .padding(start = 8.dp, end = 8.dp),
                    verticalArrangement = Arrangement.Top,
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {
                    ExpenseOverViewItem(painterResources = R.drawable.ic_housing, title = "Housing", ExpenseType.HOUSING)
                    ExpenseOverViewItem(painterResources = R.drawable.ic_food_and_beverage, title = "Food & Beverage", ExpenseType.FOOD)
                    ExpenseOverViewItem(painterResources = R.drawable.ic_clothing, title = "Clothing", ExpenseType.CLOTHING)
                    ExpenseOverViewItem(painterResources = R.drawable.ic_healthcare, title = "Healthcare", ExpenseType.HEALTHCARE)
                    ExpenseOverViewItem(painterResources = R.drawable.ic_transportation, title = "Transportation", ExpenseType.TRANSPORTATION)
                    ExpenseOverViewItem(painterResources = R.drawable.ic_other_expense, title = "Other Expense", ExpenseType.OTHER)
                }
            }
        }
    }
}

@Composable
fun TotalBalanceCard() {
    Card(
        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp),
        colors = CardDefaults.cardColors(
            containerColor = colorResource(id = R.color.light_green),
        ),
        modifier = Modifier
            .padding(16.dp)
            .fillMaxWidth()
    ) {
        Text(
            text = "Total Balance",
            modifier = Modifier
                .padding(start = 12.dp, top = 12.dp),
            textAlign = TextAlign.Center,
            color = colorResource(id = R.color.white)
        )
        Text(
            text = "฿" + formatNumberWithCommas(getSumTransactionData(TransactionType.INCOME) - getSumTransactionData(TransactionType.EXPENSE)),
            modifier = Modifier
                .padding(start = 20.dp, bottom = 8.dp),
            textAlign = TextAlign.Center,
            color = colorResource(id = R.color.white),
            fontSize = 36.sp
        )
        Text(
            text = "Income",
            modifier = Modifier
                .padding(start = 12.dp),
            textAlign = TextAlign.Center,
            color = colorResource(id = R.color.white),
            fontSize = 16.sp
        )
        Text(
            text = "฿" + formatNumberWithCommas(getSumTransactionData(TransactionType.INCOME)),
            modifier = Modifier
                .padding(start = 24.dp, bottom = 8.dp),
            textAlign = TextAlign.Center,
            color = colorResource(id = R.color.green),
            fontSize = 24.sp
        )
        Text(
            text = "Outcome",
            modifier = Modifier
                .padding(start = 12.dp),
            textAlign = TextAlign.Center,
            color = colorResource(id = R.color.white),
            fontSize = 16.sp,
        )
        Text(
            text = "฿" + formatNumberWithCommas(getSumTransactionData(TransactionType.EXPENSE)),
            modifier = Modifier
                .padding(start = 24.dp, bottom = 12.dp),
            textAlign = TextAlign.Center,
            color = colorResource(id = R.color.red),
            fontSize = 24.sp
        )
    }
}

@Composable
fun ExpenseOverViewItem(painterResources: Int, title: String, expenseType: ExpenseType) {
    var sumExpense = 0.0
    sumExpense = getSumExpenseDataByType(expenseType)
    Card( elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        modifier = Modifier
            .padding(8.dp)
            .fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = colorResource(id = R.color.yellow_white),
        )
    ) {
        Column (
            modifier = Modifier
                .fillMaxWidth(1f)
                .padding(8.dp)
        ) {
            Row {
                Image(
                    painter = painterResource(id = painterResources),
                    contentDescription = title,
                    modifier = Modifier.padding(start = 8.dp, top = 8.dp))
                Text(
                    text = title,
                    modifier = Modifier.padding(start = 8.dp, top = 12.dp),
                    fontSize = 16.sp
                )
            }
            Column {
                Text(
                    text = "฿" + formatNumberWithCommas(sumExpense),
                    modifier = Modifier.width(400.dp),
                    fontSize = 24.sp,
                    textAlign = TextAlign.End
                )
            }
        }
    }
}

@Composable
fun HomeScreenWithNavBar() {
    val navController = rememberNavController()
    var navSelectedItem by remember {
        mutableStateOf(0)
    }
    Scaffold(bottomBar = {
        NavigationBar(
            containerColor = colorResource(id = R.color.light_green),
            contentColor = colorResource(id = R.color.white),
        ) {
            ExpenseTrackerNavItemInfo().getAllNavItems().forEachIndexed { index, itemInfo ->
                NavigationBarItem(
                    selected = (index == navSelectedItem),
                    onClick = {
                        navSelectedItem = index
                        navController.navigate(itemInfo.route) {
                            popUpTo(navController.graph.findStartDestination().id) {
                                saveState = true
                            }
                            launchSingleTop = true
                            restoreState = true
                        }
                    },
                    icon = {
                        Icon(
                            imageVector = itemInfo.icon,
                            contentDescription = itemInfo.label
                        )
                    },
                    label = { Text(text = itemInfo.label) },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = colorResource(id = R.color.black),
                        selectedTextColor = colorResource(id = R.color.black),
                        unselectedIconColor = colorResource(id = R.color.black),
                        unselectedTextColor = colorResource(id = R.color.black),
                        indicatorColor = colorResource(id = R.color.pearl)
                    )
                )
            }
        }
    }) { paddingValues ->
        NavHost(
            navController = navController,
            startDestination = DestinationScreens.Home.route,
            modifier = Modifier.padding(paddingValues)
        ) {
            composable(route = DestinationScreens.Home.route) {
                HomeScreen()
            }
            composable(route = DestinationScreens.AddData.route) {
                AddDataScreen(navController)
            }
            composable(route = DestinationScreens.Overview.route) {
                OverviewScreen(navController)
            }
            composable(route = DestinationScreens.AddIncome.route) {
                AddIncomeDataScreen(navController)
            }
            composable(route = DestinationScreens.AddExpense.route) {
                AddExpenseDataScreen(navController)
            }
            composable(route = DestinationScreens.EditIncome.route) {
                EditIncomeDataScreen(navController)
            }
            composable(route = DestinationScreens.EditExpense.route) {
                EditExpenseDataScreen(navController)
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun HomeScreenPreview() {
    ExpenseTrackerAppTheme {
        HomeScreenWithNavBar()
    }
}