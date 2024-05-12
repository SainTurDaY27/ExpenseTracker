package org.classapp.expensetrackerapp.data

import java.sql.Timestamp

data class TransactionData(
    var id : String? = "",
    val note: String? = "",
    val isIncome: Boolean = false,
    val amount: Double = 0.0,
    val expenseType: ExpenseType? = null,
    val date: Timestamp? = null,
)

// enum class of expense types
enum class ExpenseType {
    HOUSING, FOOD, CLOTHING, HEALTHCARE, TRANSPORTATION, OTHER
}

enum class TransactionType {
    INCOME, EXPENSE
}