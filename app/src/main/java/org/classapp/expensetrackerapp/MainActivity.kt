package org.classapp.expensetrackerapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import org.classapp.expensetrackerapp.data.loadTransactionData
import org.classapp.expensetrackerapp.data.TransactionType
import org.classapp.expensetrackerapp.ui.theme.ExpenseTrackerAppTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //addDemoData(baseContext)
        loadTransactionData(baseContext, TransactionType.INCOME)
        loadTransactionData(baseContext, TransactionType.EXPENSE)

        setContent {
            ExpenseTrackerAppTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    HomeScreenWithNavBar()
                }
            }
        }

    }
}

@Preview(showBackground = true)
@Composable
fun MainActivityPreview() {
    ExpenseTrackerAppTheme {
        MainActivity()
    }
}