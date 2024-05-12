package org.classapp.expensetrackerapp

sealed class DestinationScreens(val route : String) {
    object Home : DestinationScreens("home")
    object AddData : DestinationScreens("addData")
    object Overview : DestinationScreens("overview")
    object AddIncome : DestinationScreens("addIncome")
    object AddExpense : DestinationScreens("addExpense")
    object EditIncome : DestinationScreens("editIncome")
    object EditExpense : DestinationScreens("editExpense")
}