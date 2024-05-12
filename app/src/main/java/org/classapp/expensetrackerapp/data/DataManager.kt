package org.classapp.expensetrackerapp.data

import android.content.Context
import java.sql.Date
import java.sql.Timestamp

var latestIncomeId: Int = 0
var latestExpenseId: Int = 0
var currentInspectTransactionDataType: TransactionType = TransactionType.INCOME
var currentInspectTransactionDataId: String = "0"
var currentDateFromDatePicker: Timestamp = Timestamp(System.currentTimeMillis())
var currentPickedExpenseType: ExpenseType = ExpenseType.OTHER
var currentDurationPicked: TimeRange = TimeRange.ONE_WEEK

val incomeDataList = mutableListOf<TransactionData>()
val expenseDataList = mutableListOf<TransactionData>()
val transactionDataList = mutableListOf<TransactionData>()

fun getLatestTransactionDataId(transactionType: TransactionType): Int {
    return when (transactionType) {
        TransactionType.INCOME -> latestIncomeId
        TransactionType.EXPENSE -> latestExpenseId
    }
}

fun getLatestId(dataList: List<TransactionData>): Int {
    var latestId = 0
    for (data in dataList) {
        if (data.id!!.toInt() > latestId) {
            latestId = data.id!!.toInt()
        }
    }
    return latestId
}

fun getSumTransactionDataEachDayByTimeRange(transactionType: TransactionType, timeRange: TimeRange): List<Double> {
    val sumList = mutableListOf<Double>()
    for (i in 0 until timeRange.days) {
        //Log.i("GetSumTransactionDataEachDayByTimeRange", "For day ${i} Current time: ${ConvertTimestampToDateString(System.currentTimeMillis() - i * 86400000)}")
        sumList.add(getSumDataByDate(transactionType, System.currentTimeMillis() - i * 86400000))
    }
    return sumList
}

fun getTransactionDataById(transactionType: TransactionType, id: String): TransactionData {
    return when (transactionType) {
        TransactionType.INCOME -> getIncomeDataById(id)
        TransactionType.EXPENSE -> getExpenseDataById(id)
    }
}

fun getTransactionDataByTimeRange(transactionType: TransactionType, timeRange: TimeRange): List<TransactionData> {
    return when (transactionType) {
        TransactionType.INCOME -> getIncomeDataByTimeRange(timeRange)
        TransactionType.EXPENSE -> getExpenseDataByTimeRange(timeRange)
    }
}

fun getTransactionDataByDate(transactionType: TransactionType, date: Long): List<TransactionData> {
    return when (transactionType) {
        TransactionType.INCOME -> incomeDataList.filter { it.date!!.time == date }
        TransactionType.EXPENSE -> expenseDataList.filter { it.date!!.time == date }
    }
}

fun getSumTransactionDataByTimeRange(transactionType: TransactionType, timeRange: TimeRange): Double {
    return when (transactionType) {
        TransactionType.INCOME -> getIncomeDataByTimeRange(timeRange).sumOf { it.amount }
        TransactionType.EXPENSE -> getExpenseDataByTimeRange(timeRange).sumOf { it.amount }
    }
}

fun getSumTransactionData(transactionType: TransactionType): Double {
    return when (transactionType) {
        TransactionType.INCOME -> incomeDataList.sumOf { it.amount }
        TransactionType.EXPENSE -> expenseDataList.sumOf { it.amount }
    }
}

fun getSumExpenseDataByType(expenseType: ExpenseType): Double {
    return expenseDataList.filter { it.expenseType == expenseType }.sumOf { it.amount }
}

fun getSumExpenseDataByTypeAndTimeRange(expenseType: ExpenseType, timeRange: TimeRange): Float {
    return getExpenseDataByTimeRange(timeRange).filter { it.expenseType == expenseType }.sumOf { it.amount }.toFloat()
}

fun getSumDataByDate(transactionType: TransactionType, date: Long): Double {
    // check if only day and month are the same
    return when (transactionType) {
        TransactionType.INCOME -> incomeDataList.filter { checkSameDateFromTimestamps(it.date!!.time, date) }.sumOf { it.amount }
        TransactionType.EXPENSE -> expenseDataList.filter { checkSameDateFromTimestamps(it.date!!.time, date) }.sumOf { it.amount }
    }
}

fun checkSameDateFromTimestamps(timestamp1: Long, timestamp2: Long): Boolean {
    return extractDayFromTimestamp(timestamp1) == extractDayFromTimestamp(timestamp2) &&
            extractMonthFromTimestamp(timestamp1) == extractMonthFromTimestamp(timestamp2) &&
            extractYearFromTimestamp(timestamp1) == extractYearFromTimestamp(timestamp2)
}

fun extractDayFromTimestamp(timestamp: Long): String {
    return Date(timestamp).toString().substring(8, 10)
}

fun extractMonthFromTimestamp(timestamp: Long): String {
    return Date(timestamp).toString().substring(5, 7)
}

fun extractYearFromTimestamp(timestamp: Long): String {
    return Date(timestamp).toString().substring(0, 4)
}

fun extractDateFromTimestamp(timestamp: Long): String
{
    return Date(timestamp).toString().substring(5, 10)
}

fun addTransactionData(context: Context, transactionType: TransactionType, transactionData: TransactionData) {
    when (transactionType) {
        TransactionType.INCOME -> addIncomeData(context, transactionData)
        TransactionType.EXPENSE -> addExpenseData(context, transactionData)
    }
}

fun updateTransactionData(context: Context, transactionType: TransactionType, transactionData: TransactionData) {
    when (transactionType) {
        TransactionType.INCOME -> updateIncomeData(context, transactionData)
        TransactionType.EXPENSE -> updateExpenseData(context, transactionData)
    }
}

fun deleteTransactionData(context: Context, transactionType: TransactionType, id: String) {
    when (transactionType) {
        TransactionType.INCOME -> deleteIncomeData(context, id)
        TransactionType.EXPENSE -> deleteExpenseData(context, id)
    }
}

fun getLatestTransactionData(amount: Int): List<TransactionData> {
    mergeTransactionData()
    return transactionDataList.takeLast(amount)
}

fun formatNumberWithCommas(number: Double): String {
    return String.format("%,.2f", number)
}

private fun mergeTransactionData() {
    transactionDataList.clear()
    transactionDataList.addAll(incomeDataList)
    transactionDataList.addAll(expenseDataList)
    transactionDataList.sortBy { it.date }
}

private fun getIncomeDataById(id: String): TransactionData {
    return incomeDataList.find { it.id == id } ?: TransactionData()
}

private fun getExpenseDataById(id: String): TransactionData {
    return expenseDataList.find { it.id == id } ?: TransactionData()
}

private fun getIncomeDataByTimeRange(timeRange: TimeRange): List<TransactionData> {
    var transactionDataList = mutableListOf<TransactionData>()
//    transactionDataList = when (timeRange) {
//        TimeRange.ONE_WEEK -> incomeDataList.filter { it.date!!.time > System.currentTimeMillis() - 604800000 }
//            .toMutableList()
//
//        TimeRange.TWO_WEEKS -> incomeDataList.filter { it.date!!.time > System.currentTimeMillis() - 1209600000 }
//            .toMutableList()
//
//        TimeRange.THREE_WEEKS -> incomeDataList.filter { it.date!!.time > System.currentTimeMillis() - 2629746000 }
//            .toMutableList()
//    }
    transactionDataList = incomeDataList.filter { it.date!!.time > System.currentTimeMillis() - timeRange.days * 86400000 }
        .toMutableList()
    return transactionDataList
}

private fun getExpenseDataByTimeRange(timeRange: TimeRange): List<TransactionData> {
    var transactionDataList = mutableListOf<TransactionData>()
//    transactionDataList = when (timeRange) {
//        TimeRange.ONE_WEEK -> expenseDataList.filter { it.date!!.time > System.currentTimeMillis() - 604800000 }
//            .toMutableList()
//
//        TimeRange.TWO_WEEKS -> expenseDataList.filter { it.date!!.time > System.currentTimeMillis() - 1209600000 }
//            .toMutableList()
//
//        // in case of one month, check if the date is within the date existing in last month or not, if not, minus more 1 day
//        TimeRange.THREE_WEEKS -> expenseDataList.filter { it.date!!.time > System.currentTimeMillis() - 2629746000 }
//            .toMutableList()
//    }
    transactionDataList = expenseDataList.filter { it.date!!.time > System.currentTimeMillis() - timeRange.days * 86400000 }
        .toMutableList()
    return transactionDataList
}

private fun addIncomeData(context: Context, transactionData: TransactionData) {
    latestIncomeId += 1
    transactionData.id = latestIncomeId.toString()
    incomeDataList.add(transactionData)
    saveTransactionData(context, TransactionType.INCOME)
}

private fun addExpenseData(context: Context, transactionData: TransactionData) {
    latestExpenseId += 1
    transactionData.id = latestExpenseId.toString()
    expenseDataList.add(transactionData)
    saveTransactionData(context, TransactionType.EXPENSE)
}

private fun updateIncomeData(context: Context, transactionData: TransactionData) {
    val index = incomeDataList.indexOfFirst { it.id == transactionData.id }
    incomeDataList[index] = transactionData
    saveTransactionData(context, TransactionType.INCOME)
}

private fun updateExpenseData(context: Context, transactionData: TransactionData) {
    val index = expenseDataList.indexOfFirst { it.id == transactionData.id }
    expenseDataList[index] = transactionData
    saveTransactionData(context, TransactionType.EXPENSE)
}

private fun deleteIncomeData(context: Context, id: String) {
    incomeDataList.removeIf { it.id == id }
    saveTransactionData(context, TransactionType.INCOME)
}

private fun deleteExpenseData(context: Context, id: String) {
    expenseDataList.removeIf { it.id == id }
    saveTransactionData(context, TransactionType.EXPENSE)
}

enum class TimeRange(val days: Int) {
    ONE_WEEK(7),
    TWO_WEEKS(14),
    THREE_WEEKS(21)
}