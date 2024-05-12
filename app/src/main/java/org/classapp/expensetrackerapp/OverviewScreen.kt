package org.classapp.expensetrackerapp

import android.text.TextUtils
import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
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
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import co.yml.charts.axis.AxisData
import co.yml.charts.common.components.Legends
import co.yml.charts.common.model.PlotType
import org.classapp.expensetrackerapp.ui.theme.ExpenseTrackerAppTheme

import co.yml.charts.common.model.Point
import co.yml.charts.common.utils.DataUtils
import co.yml.charts.ui.linechart.LineChart
import co.yml.charts.ui.linechart.model.GridLines
import co.yml.charts.ui.linechart.model.IntersectionPoint
import co.yml.charts.ui.linechart.model.Line
import co.yml.charts.ui.linechart.model.LineChartData
import co.yml.charts.ui.linechart.model.LinePlotData
import co.yml.charts.ui.linechart.model.LineStyle
import co.yml.charts.ui.linechart.model.LineType
import co.yml.charts.ui.linechart.model.SelectionHighlightPopUp
import co.yml.charts.ui.piechart.charts.PieChart
import co.yml.charts.ui.piechart.models.PieChartConfig
import co.yml.charts.ui.piechart.models.PieChartData
import org.classapp.expensetrackerapp.data.ExpenseType
import org.classapp.expensetrackerapp.data.getSumExpenseDataByTypeAndTimeRange
import org.classapp.expensetrackerapp.data.getSumTransactionDataEachDayByTimeRange
import org.classapp.expensetrackerapp.data.TimeRange
import org.classapp.expensetrackerapp.data.TransactionType
import org.classapp.expensetrackerapp.data.currentDurationPicked
import org.classapp.expensetrackerapp.data.extractDateFromTimestamp


@Composable
fun OverviewScreen(navController: NavController) {
    ExpenseTrackerAppTheme (dynamicColor = false) {
        Surface(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState()),
            color = MaterialTheme.colorScheme.background
        ) {
            Column (
                modifier = Modifier
                    .fillMaxSize(),
                verticalArrangement = Arrangement.Top,
                horizontalAlignment = Alignment.Start,
            )
            {
                Column (
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(start = 10.dp, top = 10.dp),
                ) {
                    Row (
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(start = 10.dp, top = 10.dp)
                    )
                    {
                        TimeDurationDropdown(navController)
                    }
                }
            }
            Column (
                modifier = Modifier
                    .fillMaxSize()
                    .padding(top = 80.dp, start = 20.dp)
            ) {
                Row {
                    Text(
                        text = "Total Income:",
                        modifier = Modifier.padding(top = 10.dp),
                        fontWeight = FontWeight.Bold,
                        fontSize = 25.sp
                    )
                    Text(
                    buildAnnotatedString {
                        withStyle(style = SpanStyle(Color.Green)) {
                            append(" ฿${getSumTransactionDataEachDayByTimeRange(TransactionType.INCOME, currentDurationPicked).sum()}")
                        }
                    },
                        modifier = Modifier.padding(top = 10.dp),
                        fontWeight = FontWeight.Bold,
                        fontSize = 25.sp
                    )
                }
                Text(
                    text = "Horizontally drag to inspect a chart.",
                    modifier = Modifier.padding(bottom = 20.dp),
                    color = Color.Gray
                )
                TransactionDataLineChart(TransactionType.INCOME, currentDurationPicked)
                Row (
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween,
                ){
                    Text(
                        text = "Total Expense:",
                        modifier = Modifier.padding(top = 10.dp),
                        fontWeight = FontWeight.Bold,
                        fontSize = 25.sp
                    )
                    Text(
                        buildAnnotatedString {
                            withStyle(style = SpanStyle(Color.Red)) {
                                append(" ฿${getSumTransactionDataEachDayByTimeRange(TransactionType.EXPENSE, currentDurationPicked).sum()}")
                            }
                        },
                        modifier = Modifier.padding(top = 10.dp),
                        fontWeight = FontWeight.Bold,
                        fontSize = 25.sp
                    )
                }
                Text(
                    text = "Horizontally drag to inspect a chart.",
                    modifier = Modifier.padding(bottom = 20.dp),
                    color = Color.Gray
                )
                TransactionDataLineChart(TransactionType.EXPENSE, currentDurationPicked)
                Text (
                    text = "Expense Statistics",
                    modifier = Modifier.padding(top = 10.dp),
                    fontWeight = FontWeight.Bold,
                    fontSize = 25.sp
                )
                Text(
                    text = "Based on each expense types",
                    color = Color.Gray
                )
                ExpenseStatisticGraph(currentDurationPicked)
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TimeDurationDropdown(navController: NavController)
{
    val context = LocalContext.current
    val timeRange = listOf("1 Week", "2 Weeks", "3 Weeks")
    var expanded by remember { mutableStateOf(false) }
    var selectedTimeRange = when (currentDurationPicked)
    {
        TimeRange.ONE_WEEK -> "1 Week"
        TimeRange.TWO_WEEKS -> "2 Weeks"
        TimeRange.THREE_WEEKS -> "3 Weeks"
        else -> "1 Week"
    }

    Box (
        modifier = Modifier
            //.fillMaxSize()
            .padding(start = 180.dp)
            .size(width = 170.dp, height = 50.dp)
            .background(colorResource(id = R.color.dark_pearl))
            .border(1.dp, Color.Black, MaterialTheme.shapes.extraSmall)
    ) {
        ExposedDropdownMenuBox(
            expanded = expanded,
            onExpandedChange = {
                expanded = !expanded
            }
        ) {
            TextField(
                value = selectedTimeRange,
                onValueChange = {},
                readOnly = true,
                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
                modifier = Modifier.menuAnchor(),
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = colorResource(id = R.color.dark_pearl),
                    unfocusedContainerColor = colorResource(id = R.color.dark_pearl)
                )
            )

            ExposedDropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false }
            ) {
                timeRange.forEach { item ->
                    DropdownMenuItem(
                        text = { Text(text = item) },
                        onClick = {
                            expanded = false
                            currentDurationPicked = when (item) {
                                "1 Week" -> TimeRange.ONE_WEEK
                                "2 Weeks" -> TimeRange.TWO_WEEKS
                                "3 Weeks" -> TimeRange.THREE_WEEKS
                                else -> TimeRange.ONE_WEEK
                            }
                            selectedTimeRange = item
                            // refresh the screen
                            navController.navigate(DestinationScreens.Overview.route)
                        },
                    )
                }
            }
        }
    }
}

fun convertTransactionDataListToPoints(sumTransactionDataList: List<Double>): List<Point>
{
    val points = mutableListOf<Point>()
    var dataIndex = 0;
    for (sumTransactionData in sumTransactionDataList)
    {
        points.add(Point(dataIndex.toFloat(), sumTransactionData.toFloat()))
        dataIndex += 1
    }
    return points
}

fun findMaxTransactionValue(points: List<Point>): Float
{
    var maxTransactionValue = 0f
    for (point in points)
    {
        if (point.y > maxTransactionValue)
        {
            maxTransactionValue = point.y
        }
    }
    return maxTransactionValue
}

@Composable
fun TransactionDataLineChart(transactionType: TransactionType, timeRange: TimeRange)
{
    val backgroundColor = if (transactionType == TransactionType.INCOME) Color(0xFFDFEFD0) else Color(0xFFFFCFCA)
    val days = timeRange.days
    var dataFromTimeRange = listOf<Point>()
    dataFromTimeRange = convertTransactionDataListToPoints(getSumTransactionDataEachDayByTimeRange(
        transactionType, timeRange).reversed())
    val maxTransactionValue = findMaxTransactionValue(dataFromTimeRange)

    val startDate = System.currentTimeMillis() - (days - 1) * 86400000
    Log.i("IncomeAndOutcomeLineChart", "Start date at: ${extractDateFromTimestamp(startDate)} when $currentDurationPicked is picked")
    val dateLabelList = mutableListOf<String>()
    for (i in 0 until days)
    {
        dateLabelList.add(extractDateFromTimestamp(startDate + i * 86400000))
        // log date label side with data points
        Log.i("IncomeAndOutcomeLineChart", "Date label at day $i: ${extractDateFromTimestamp(startDate + i * 86400000)} + data point: ${dataFromTimeRange[i]}")
    }

    val chartColor = if (transactionType == TransactionType.INCOME) Color.Green else Color.Red

    val steps = 5

    val xAxisData = AxisData.Builder()
        .axisStepSize(100.dp)
        .backgroundColor(backgroundColor)
        .steps(dataFromTimeRange.size - 1)
        .labelData { i -> dateLabelList[i] }
        .labelAndAxisLinePadding(15.dp)
        .build()

    val yAxisData = AxisData.Builder()
        .steps(steps)
        .backgroundColor(backgroundColor)
        .labelAndAxisLinePadding(20.dp)
        .labelData { i ->
            val yScale = maxTransactionValue / steps
            (i * yScale).toString()
        }
        .build()

    val lineChartData = LineChartData(
        linePlotData = LinePlotData(
            lines = listOf(
                Line(
                    dataPoints = dataFromTimeRange,
                    lineStyle = LineStyle(lineType = LineType.Straight(), color = chartColor),
                    intersectionPoint = IntersectionPoint(),
                    selectionHighlightPopUp = SelectionHighlightPopUp(popUpLabel = { x, y ->
                        val xLabel = "x : ${(1900 + x).toInt()} "
                        val yLabel = "y : ${String.format("%.2f", y)}"
                        "$xLabel $yLabel"
                    })
                ),
            )
        ),
        xAxisData = xAxisData,
        yAxisData = yAxisData,
        gridLines = GridLines(),
        backgroundColor = colorResource(id = R.color.dark_pearl)
    )

    Text(text = "Money(฿)")
    LineChart(
        modifier = Modifier
            .fillMaxSize()
            //.fillMaxWidth()
            .height(300.dp),
        lineChartData = lineChartData,
    )
    Column (
        modifier = Modifier
            .fillMaxSize()
            .padding(end = 10.dp, bottom = 20.dp),
        horizontalAlignment = Alignment.End
    )
    {
        Text(text = "Time(Days)")
    }
}

@Composable
fun ExpenseStatisticGraph(timeRange: TimeRange)
{
    val expensePieChartData = PieChartData(
        slices = listOf(
            PieChartData.Slice("Housing", value = getSumExpenseDataByTypeAndTimeRange(ExpenseType.HOUSING, timeRange), Color(0xFFFFCFCA)),
            PieChartData.Slice("Food", value = getSumExpenseDataByTypeAndTimeRange(ExpenseType.FOOD, timeRange), Color(0xFFCEC2EB)),
            PieChartData.Slice("Clothing", value = getSumExpenseDataByTypeAndTimeRange(ExpenseType.CLOTHING, timeRange), Color(0xFFABBDEE)),
            PieChartData.Slice("Healthcare", value = getSumExpenseDataByTypeAndTimeRange(ExpenseType.HEALTHCARE, timeRange), Color(0xFFC6E0E0)),
            PieChartData.Slice("Transportation", value = getSumExpenseDataByTypeAndTimeRange(ExpenseType.TRANSPORTATION, timeRange), Color(0xFFDFEFD0)),
            PieChartData.Slice("Other", value = getSumExpenseDataByTypeAndTimeRange(ExpenseType.OTHER, timeRange), Color(0xFFFFE2AE))
        ),
        plotType = PlotType.Pie
    )

    val pieChartConfig = PieChartConfig(
        labelVisible = true,
        activeSliceAlpha = 0.9F,
        isEllipsizeEnabled = true,
        sliceLabelEllipsizeAt = TextUtils.TruncateAt.MIDDLE,
        isAnimationEnable = true,
        chartPadding = 30,
        showSliceLabels = true,
        animationDuration = 1600,
        isSumVisible = true,
        sliceLabelTextColor = Color(0xFF404565),
        backgroundColor = colorResource(id = R.color.dark_pearl)
    )

    Column(
        modifier = Modifier
            .height(500.dp)
    )
    {
        PieChart(
            modifier = Modifier
                .fillMaxWidth(),
            pieChartData = expensePieChartData,
            pieChartConfig = pieChartConfig,
        )
        Legends(legendsConfig = DataUtils.getLegendsConfigFromPieChartData(expensePieChartData, 3))
    }
}

@Preview
@Composable
fun OverviewScreenPreview() {
    OverviewScreen(navController = rememberNavController())
}