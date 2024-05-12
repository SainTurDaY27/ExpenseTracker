package org.classapp.expensetrackerapp

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Star
import androidx.compose.ui.graphics.vector.ImageVector

data class ExpenseTrackerNavItemInfo(
    val label : String = "",
    val icon : ImageVector = Icons.Filled.Star,
    val route : String = ""
) {
    fun getAllNavItems() : List<ExpenseTrackerNavItemInfo> {
        return listOf(
            ExpenseTrackerNavItemInfo("Home", Icons.Filled.Home, DestinationScreens.Home.route),
            ExpenseTrackerNavItemInfo("Add Data", Icons.Filled.Add, DestinationScreens.AddData.route),
            ExpenseTrackerNavItemInfo("Overview", Icons.Filled.List, DestinationScreens.Overview.route)
        )
    }
}
