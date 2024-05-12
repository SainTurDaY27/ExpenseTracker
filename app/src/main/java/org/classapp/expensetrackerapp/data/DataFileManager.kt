package org.classapp.expensetrackerapp.data

import android.content.Context
import android.util.Log
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.JsonParser
import java.io.FileInputStream
import java.io.FileOutputStream
import java.io.InputStreamReader
import java.sql.Timestamp

const val incomeDataFile:String = "incomeData"
const val expenseDataFile:String = "expenseData"

fun addDemoData(context: Context) {
    val mockUpIncomeData1 = TransactionData(id = "1", note = "Salary", isIncome = true, amount = 10000.0, date = Timestamp.valueOf("2024-5-01 00:00:00"))
    val mockUpIncomeData2 = TransactionData(id = "2", note = "From Dad", isIncome = true, amount = 2000.0, date = Timestamp.valueOf("2024-5-02 09:00:00"))
    val mockUpIncomeData3 = TransactionData(id = "3", note = "From Mom", isIncome = true, amount = 1500.0, date = Timestamp.valueOf("2024-5-04 10:00:00"))
    val mockUpIncomeData4 = TransactionData(id = "4", note = "From Dad2", isIncome = true, amount = 3000.0, date = Timestamp.valueOf("2024-5-07 09:00:00"))
    val mockUpIncomeData5 = TransactionData(id = "5", note = "From Mom2", isIncome = true, amount = 500.0, date = Timestamp.valueOf("2024-5-09 10:30:00"))
    val mockUpIncomeData6 = TransactionData(id = "6", note = "Mini salary", isIncome = true, amount = 15000.0, date = Timestamp.valueOf("2024-5-10 09:00:00"))

    val mockUpExpenseData1 = TransactionData(id = "1", note = "Electricity", isIncome = false, amount = 1200.0, expenseType = ExpenseType.HOUSING, date = Timestamp.valueOf("2024-05-01 04:00:00"))
    val mockUpExpenseData2 = TransactionData(id = "2", note = "Pizza", isIncome = false, amount = 200.0, expenseType = ExpenseType.FOOD, date = Timestamp.valueOf("2024-05-02 13:00:00"))
    val mockUpExpenseData3 = TransactionData(id = "3", note = "New shirt", isIncome = false, amount = 150.0, expenseType = ExpenseType.CLOTHING, date = Timestamp.valueOf("2024-05-02 16:00:00"))
    val mockUpExpenseData4 = TransactionData(id = "4", note = "Vitamin", isIncome = false, amount = 500.0, expenseType = ExpenseType.HEALTHCARE, date = Timestamp.valueOf("2024-05-05 10:00:00"))
    val mockUpExpenseData5 = TransactionData(id = "5", note = "Taxi", isIncome = false, amount = 120.0, expenseType = ExpenseType.TRANSPORTATION, date = Timestamp.valueOf("2024-05-06 16:00:00"))
    val mockUpExpenseData6 = TransactionData(id = "6", note = "Buffet", isIncome = false, amount = 27.0, expenseType = ExpenseType.OTHER, date = Timestamp.valueOf("2024-05-07 14:00:00"))
    val mockUpExpenseData7 = TransactionData(id = "7", note = "Noodle", isIncome = false, amount = 150.0, expenseType = ExpenseType.FOOD, date = Timestamp.valueOf("2024-05-07 14:00:00"))
    val mockUpExpenseData8 = TransactionData(id = "8", note = "Shoes", isIncome = false, amount = 300.0, expenseType = ExpenseType.CLOTHING, date = Timestamp.valueOf("2024-05-08 14:00:00"))
    val mockUpExpenseData9 = TransactionData(id = "9", note = "Medicine", isIncome = false, amount = 300.0, expenseType = ExpenseType.HEALTHCARE, date = Timestamp.valueOf("2024-05-09 14:00:00"))
    val mockUpExpenseData10 = TransactionData(id = "10", note = "Bus", isIncome = false, amount = 50.0, expenseType = ExpenseType.TRANSPORTATION, date = Timestamp.valueOf("2024-05-10 14:00:00"))
    val mockUpExpenseData11 = TransactionData(id = "11", note = "Mobile bill", isIncome = false, amount = 270.0, expenseType = ExpenseType.OTHER, date = Timestamp.valueOf("2024-05-10 14:00:00"))
    
    val demoIncomeDataList = mutableListOf<TransactionData>()
    demoIncomeDataList.add(mockUpIncomeData1)
    demoIncomeDataList.add(mockUpIncomeData2)
    demoIncomeDataList.add(mockUpIncomeData3)
    demoIncomeDataList.add(mockUpIncomeData4)
    demoIncomeDataList.add(mockUpIncomeData5)
    demoIncomeDataList.add(mockUpIncomeData6)

    val demoExpenseDataList = mutableListOf<TransactionData>()
    demoExpenseDataList.add(mockUpExpenseData1)
    demoExpenseDataList.add(mockUpExpenseData2)
    demoExpenseDataList.add(mockUpExpenseData3)
    demoExpenseDataList.add(mockUpExpenseData4)
    demoExpenseDataList.add(mockUpExpenseData5)
    demoExpenseDataList.add(mockUpExpenseData6)
    demoExpenseDataList.add(mockUpExpenseData7)
    demoExpenseDataList.add(mockUpExpenseData8)
    demoExpenseDataList.add(mockUpExpenseData9)
    demoExpenseDataList.add(mockUpExpenseData10)
    demoExpenseDataList.add(mockUpExpenseData11)

    // convert to json string without using gsonPretty
    val gson = Gson()
    val incomeJsonString:String = gson.toJson(demoIncomeDataList)
    val expenseJsonString:String = gson.toJson(demoExpenseDataList)

    Log.i("AddDemoData", "Income json string: $incomeJsonString")
    //val prettyExpenseJsonString:String = gsonPretty.toJson(demoTransactionDataList)
    Log.i("AddDemoData", "Expense json string: $expenseJsonString")

    var fileOutputStream: FileOutputStream

    try {
        fileOutputStream = context.openFileOutput(incomeDataFile, Context.MODE_PRIVATE)
        fileOutputStream.write(incomeJsonString.toByteArray())
        Log.i("AddIncomeDemoData", "Successfully wrote to file $incomeDataFile")

        fileOutputStream = context.openFileOutput(expenseDataFile, Context.MODE_PRIVATE)
        fileOutputStream.write(expenseJsonString.toByteArray())
        Log.i("AddExpenseDemoData", "Successfully wrote to file $expenseDataFile")

    } catch (e: Exception) {
        e.printStackTrace()
    }
}

fun saveTransactionData(context: Context, transactionType: TransactionType) {
    val gsonPretty = GsonBuilder().setPrettyPrinting().create()
    val prettyJsonString:String = when (transactionType) {
        TransactionType.INCOME -> gsonPretty.toJson(incomeDataList)
        TransactionType.EXPENSE -> gsonPretty.toJson(expenseDataList)
    }
    val fileOutputStream: FileOutputStream

    try {
        fileOutputStream = when (transactionType) {
            TransactionType.INCOME -> context.openFileOutput(incomeDataFile, Context.MODE_PRIVATE)
            TransactionType.EXPENSE -> context.openFileOutput(expenseDataFile, Context.MODE_PRIVATE)
        }
        fileOutputStream.write(prettyJsonString.toByteArray())
        Log.i("SaveTransactionData", "Successfully wrote $transactionType data")
    } catch (e: Exception) {
        e.printStackTrace()
    }
}

fun loadTransactionData(context: Context, transactionType: TransactionType) {
    var fileInputStream: FileInputStream? = null
    fileInputStream = when (transactionType) {
        TransactionType.INCOME -> context.openFileInput(incomeDataFile)
        TransactionType.EXPENSE -> context.openFileInput(expenseDataFile)
    }
    Log.i("LoadTransactionData", "File input stream $fileInputStream")
    val inputStreamReader: InputStreamReader = InputStreamReader(fileInputStream)
    val stringBuilder: StringBuilder = StringBuilder()
    var text: String? = null
    val bufferReader = inputStreamReader.buffered()
    while (run {
            text = bufferReader.readLine()
            text
        } != null) {
        stringBuilder.append(text)
    }
    val jsonArray = JsonParser.parseString(stringBuilder.toString()).asJsonArray

    // log length of jsonArray
    Log.i("LoadTransactionData", "Length of jsonArray: ${jsonArray.size()}")

    for (jsonElement in jsonArray) {
        val transactionData = Gson().fromJson(jsonElement, TransactionData::class.java)
        Log.i("LoadTransactionData", "Loaded $transactionType data: ${transactionData}")
        when (transactionType) {
            TransactionType.INCOME -> incomeDataList.add(transactionData)
            TransactionType.EXPENSE -> expenseDataList.add(transactionData)
        }
    }

    when (transactionType) {
        TransactionType.INCOME -> latestIncomeId = getLatestId(incomeDataList)
        TransactionType.EXPENSE -> latestExpenseId = getLatestId(expenseDataList)
    }
    Log.i("LoadTransactionData", "Successfully updated latest id for $transactionType data: ${getLatestId(getTransactionDataList(transactionType))}")
}

fun getTransactionDataList(transactionType: TransactionType): MutableList<TransactionData> {
    return when (transactionType) {
        TransactionType.INCOME -> incomeDataList
        TransactionType.EXPENSE -> expenseDataList
    }
}

fun clearDataFromPath(context: Context, path: String) {
    context.deleteFile(path)
    Log.i("ClearDataFromPath", "Successfully cleared data from $path")
}
