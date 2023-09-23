package com.tnmd.learningenglishapp.screens.extension

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.Button
import androidx.compose.material.ButtonColors
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Switch
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ChevronLeft
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.input.key.Key.Companion.H
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.YearMonth
import com.tnmd.learningenglishapp.R.string as AppText

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ScheduleScreen(viewModel: ExtensionViewModel = hiltViewModel()) {
    var selectedDate by remember { mutableStateOf(LocalDate.now()) }
    val uiState by viewModel.uiState.collectAsState()
    val yearMonth = remember { mutableStateOf(YearMonth.now()) }

    Log.d("checkSelectedDate", selectedDate.toString())

    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Month and Year Header
        TopAppBar(
            title = {
                Text(
                    text = "${yearMonth.value.month} ${yearMonth.value.year}",
                    style = MaterialTheme.typography.h6
                )
            },
            navigationIcon = {
                IconButton(
                    onClick = {
                        yearMonth.value = yearMonth.value.minusMonths(1)
                    }
                ) {
                    Icon(imageVector = Icons.Default.ChevronLeft, contentDescription = null)
                }
            },
            actions = {
                IconButton(
                    onClick = {
                        yearMonth.value = yearMonth.value.plusMonths(1)
                    }
                ) {
                    Icon(imageVector = Icons.Default.ChevronRight, contentDescription = null)
                }
            }
        )

        // Calendar Days
        val daysInMonth = yearMonth.value.lengthOfMonth()
        val firstDayOfMonth = yearMonth.value.atDay(1).dayOfWeek
        val daysOfWeek = listOf(DayOfWeek.SUNDAY, DayOfWeek.MONDAY, DayOfWeek.TUESDAY, DayOfWeek.WEDNESDAY, DayOfWeek.THURSDAY, DayOfWeek.FRIDAY, DayOfWeek.SATURDAY)
        val allDays = mutableListOf<Pair<String, Boolean>>()

        // Ngày cuối của tháng trước
        for (i in 1 until firstDayOfMonth.value+1) {
            allDays.add("" to false)
        }

        // Ngày trong tháng
        for (i in 1..daysInMonth) {
            allDays.add(i.toString() to false)
        }

        // Ngày bắt đầu của tháng sau
        val remainingDays = 7 - (allDays.size % 7)
        for (i in 1..remainingDays) {
            allDays.add("" to true)
        }

        // Ngày đầu tiên của tháng trước đó
        for (i in 1 until firstDayOfMonth.value) {
            allDays.add("" to true)
        }

        Column {
            // Days of the week header
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(2.dp)
                    .align(Alignment.CenterHorizontally),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                for (day in daysOfWeek) {
                    Text(
                        text = day.toString().substring(0, 3), // Lấy ba ký tự đầu để hiển thị ngắn gọn
                        style = MaterialTheme.typography.caption,
                        modifier = Modifier
                            .weight(1f)
                            .padding(vertical = 4.dp, horizontal = 11.dp)
                    )
                }
            }
            // Calendar Days
            for (i in 0 until allDays.size step 7) {
                val weekEndIndex = minOf(i + 7, allDays.size)

                val week = allDays.subList(i, weekEndIndex)
                Spacer(modifier = Modifier.padding(4.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {

                    for ((day, isHidden) in week) {
                        if (!isHidden) {
                            val isToday = if (day.isNotEmpty()) {
                                selectedDate == LocalDate.of(yearMonth.value.year, yearMonth.value.monthValue, day.toInt())
                            } else {
                                false
                            }
                            val buttonColors = if (isToday) {
                                ButtonDefaults.buttonColors(backgroundColor = Color.Yellow)
                            } else {
                                ButtonDefaults.buttonColors(backgroundColor = Color.Transparent)
                            }
                            val textColor = if (isToday) Color.Black else Color.Black

                            val content = if (day.isNotEmpty()) {
                                day
                            } else {
                                ""
                            }
                            Spacer(modifier = Modifier.padding(2.dp))
                            Button(
                                onClick = {
                                    if (day.isNotEmpty() && uiState.isEditSchedule == true) {
                                        selectedDate = LocalDate.of(yearMonth.value.year, yearMonth.value.monthValue, day.toInt())
                                        viewModel.updateDayUserChoosen(selectedDate.toString())
                                    }
                                },
                                colors = buttonColors,
                                modifier = Modifier
                                    .weight(1f)
                                    .fillMaxWidth()
                            ) {
                                Text(
                                    text = content,
                                    style = TextStyle(
                                        color = textColor,
                                        fontWeight = FontWeight.Bold,
                                        textAlign = TextAlign.Center
                                    )
                                )
                            }
                        } else {
                            Spacer(modifier = Modifier.weight(1f))
                        }
                    }
                }
            }
        }
        ToggleExample(viewModel = viewModel)
    }

}

@Composable
fun ToggleExample(viewModel : ExtensionViewModel = hiltViewModel()) {
    var isChecked by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(text = stringResource(id = AppText.Edit_Schedule) , fontSize = 20.sp)

            // Toggle switch
            Switch(
                checked = isChecked,
                onCheckedChange = {
                    isChecked = it
                    // Handle the toggle state change here
                    if (isChecked) {
                        viewModel.updateEdit(isChecked)
                    } else {
                        viewModel.updateEdit(isChecked)
                    }
                }
            )
        }
    }
}


@Preview
@Composable
fun CalendarWithTodayHighlightPreview() {
    ScheduleScreen()
}