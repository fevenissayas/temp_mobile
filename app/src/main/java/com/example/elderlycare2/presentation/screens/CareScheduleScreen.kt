package com.example.elderlycare2.presentation.screens

import android.app.TimePickerDialog
import android.app.DatePickerDialog
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.elderlycare2.presentation.state.CareScheduleEvent
import com.example.elderlycare2.presentation.viewmodel.CareScheduleViewModel
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun CareScheduleScreen(
    navController: NavController,
    careScheduleViewModel: CareScheduleViewModel = hiltViewModel(),
) {
    val careScheduleState by careScheduleViewModel.careScheduleState.collectAsState()
    val context = LocalContext.current

    var showPostToDropdown by remember { mutableStateOf(false) }
    var showStartTimePicker by remember { mutableStateOf(false) }
    var showEndTimePicker by remember { mutableStateOf(false) }
    var showFrequencyDropdown by remember { mutableStateOf(false) }
    var showConfirmDialog by remember { mutableStateOf(false) }

    // Frequency options for dropdown
    val frequencyOptions = listOf("Every 8hrs", "Daily", "Weekly", "Monthly")

    // Fetch users for "Post To" drop down on screen composition
    LaunchedEffect(Unit) {
        careScheduleViewModel.fetchUserList()
    }

    // Helper to show both date and time picker, then combine their result
    fun pickDateTime(
        onResult: (String) -> Unit,
        initial: String = ""
    ) {
        val calendar = Calendar.getInstance()
        if (initial.isNotBlank()) {
            try {
                val sdf = SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.US)
                calendar.time = sdf.parse(initial) ?: Date()
            } catch (_: Exception) {}
        }
        DatePickerDialog(
            context,
            { _, year, month, day ->
                calendar.set(Calendar.YEAR, year)
                calendar.set(Calendar.MONTH, month)
                calendar.set(Calendar.DAY_OF_MONTH, day)
                // After date picked, show time picker
                TimePickerDialog(
                    context,
                    { _, hour, minute ->
                        calendar.set(Calendar.HOUR_OF_DAY, hour)
                        calendar.set(Calendar.MINUTE, minute)
                        val formatted =
                            SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.US).format(calendar.time)
                        onResult(formatted)
                    },
                    calendar.get(Calendar.HOUR_OF_DAY),
                    calendar.get(Calendar.MINUTE),
                    true
                ).show()
            },
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        ).show()
    }

    if (showConfirmDialog) {
        AlertDialog(
            onDismissRequest = { showConfirmDialog = false },
            title = { Text("Confirm Submission") },
            text = { Text("Are you sure you want to submit this care schedule?") },
            confirmButton = {
                TextButton(
                    onClick = {
                        showConfirmDialog = false
                        careScheduleViewModel.handleEvent(CareScheduleEvent.OnSubmit)
                    }
                ) { Text("Confirm") }
            },
            dismissButton = {
                TextButton(onClick = { showConfirmDialog = false }) {
                    Text("Cancel")
                }
            }
        )
    }

    // Handle successful submission: go back
    LaunchedEffect(careScheduleState.isSuccess) {
        if (careScheduleState.isSuccess) {
            navController.popBackStack()
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(horizontal = 24.dp)
    ) {
        Spacer(modifier = Modifier.height(32.dp))
        Text(
            text = "Care Schedule",
            fontSize = 28.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.align(Alignment.CenterHorizontally)
        )
        Spacer(modifier = Modifier.height(36.dp))

        // Care Plan
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(bottom = 32.dp)
        ) {
            Text(
                text = "Care Plan",
                fontSize = 18.sp,
                modifier = Modifier.weight(1f)
            )
            TextField(
                value = careScheduleState.carePlan,
                onValueChange = { careScheduleViewModel.handleEvent(CareScheduleEvent.OnCarePlanChange(it)) },
                colors = TextFieldDefaults.colors(
                    unfocusedContainerColor = Color(0xFFE7F0EA),
                    focusedContainerColor = Color(0xFFE7F0EA),
                    unfocusedIndicatorColor = Color.Transparent,
                    focusedIndicatorColor = Color.Transparent,
                    disabledIndicatorColor = Color.Transparent
                ),
                shape = RoundedCornerShape(16.dp),
                modifier = Modifier
                    .weight(2f)
                    .height(56.dp),
                singleLine = true,
                textStyle = LocalTextStyle.current.copy(fontSize = 18.sp)
            )
        }

        // Frequency
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(bottom = 32.dp)
        ) {
            Text(
                text = "Frequency",
                fontSize = 18.sp,
                modifier = Modifier.weight(1f)
            )
            Box(
                modifier = Modifier
                    .weight(2f)
                    .height(56.dp)
            ) {
                TextField(
                    value = careScheduleState.frequency,
                    onValueChange = {},
                    colors = TextFieldDefaults.colors(
                        unfocusedContainerColor = Color(0xFFE7F0EA),
                        focusedContainerColor = Color(0xFFE7F0EA),
                        unfocusedIndicatorColor = Color.Transparent,
                        focusedIndicatorColor = Color.Transparent,
                        disabledIndicatorColor = Color.Transparent
                    ),
                    shape = RoundedCornerShape(16.dp),
                    modifier = Modifier.fillMaxSize(),
                    trailingIcon = {
                        Icon(
                            imageVector = Icons.Default.KeyboardArrowDown,
                            contentDescription = "Pick frequency",
                            modifier = Modifier.clickable { showFrequencyDropdown = true }
                        )
                    },
                    readOnly = true,
                    singleLine = true,
                    textStyle = LocalTextStyle.current.copy(fontSize = 18.sp)
                )
                DropdownMenu(
                    expanded = showFrequencyDropdown,
                    onDismissRequest = { showFrequencyDropdown = false }
                ) {
                    frequencyOptions.forEach { option ->
                        DropdownMenuItem(
                            text = { Text(option) },
                            onClick = {
                                careScheduleViewModel.handleEvent(CareScheduleEvent.OnFrequencyChange(option))
                                showFrequencyDropdown = false
                            }
                        )
                    }
                }
            }
        }

        // Start Time
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(bottom = 32.dp)
        ) {
            Text(
                text = "Start Time",
                fontSize = 18.sp,
                modifier = Modifier.weight(1f)
            )
            Box(
                modifier = Modifier
                    .weight(2f)
                    .height(56.dp)
            ) {
                TextField(
                    value = careScheduleState.startTime,
                    onValueChange = {},
                    readOnly = true,
                    colors = TextFieldDefaults.colors(
                        unfocusedContainerColor = Color(0xFFE7F0EA),
                        focusedContainerColor = Color(0xFFE7F0EA),
                        unfocusedIndicatorColor = Color.Transparent,
                        focusedIndicatorColor = Color.Transparent,
                        disabledIndicatorColor = Color.Transparent
                    ),
                    shape = RoundedCornerShape(16.dp),
                    trailingIcon = {
                        Icon(
                            imageVector = Icons.Default.KeyboardArrowDown,
                            contentDescription = "Pick start time",
                            modifier = Modifier.clickable { showStartTimePicker = true }
                        )
                    },
                    modifier = Modifier.fillMaxSize(),
                    singleLine = true,
                    textStyle = LocalTextStyle.current.copy(fontSize = 18.sp)
                )
                if (showStartTimePicker) {
                    LaunchedEffect(Unit) {
                        pickDateTime(
                            onResult = {
                                careScheduleViewModel.handleEvent(CareScheduleEvent.OnStartTimeChange(it))
                                showStartTimePicker = false
                            },
                            initial = careScheduleState.startTime
                        )
                    }
                }
            }
        }

        // End Time
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(bottom = 36.dp)
        ) {
            Text(
                text = "End Time",
                fontSize = 18.sp,
                modifier = Modifier.weight(1f)
            )
            Box(
                modifier = Modifier
                    .weight(2f)
                    .height(56.dp)
            ) {
                TextField(
                    value = careScheduleState.endTime,
                    onValueChange = {},
                    readOnly = true,
                    colors = TextFieldDefaults.colors(
                        unfocusedContainerColor = Color(0xFFE7F0EA),
                        focusedContainerColor = Color(0xFFE7F0EA),
                        unfocusedIndicatorColor = Color.Transparent,
                        focusedIndicatorColor = Color.Transparent,
                        disabledIndicatorColor = Color.Transparent
                    ),
                    shape = RoundedCornerShape(16.dp),
                    trailingIcon = {
                        Icon(
                            imageVector = Icons.Default.KeyboardArrowDown,
                            contentDescription = "Pick end time",
                            modifier = Modifier.clickable { showEndTimePicker = true }
                        )
                    },
                    modifier = Modifier.fillMaxSize(),
                    singleLine = true,
                    textStyle = LocalTextStyle.current.copy(fontSize = 18.sp)
                )
                if (showEndTimePicker) {
                    LaunchedEffect(Unit) {
                        pickDateTime(
                            onResult = {
                                careScheduleViewModel.handleEvent(CareScheduleEvent.OnEndTimeChange(it))
                                showEndTimePicker = false
                            },
                            initial = careScheduleState.endTime
                        )
                    }
                }
            }
        }

        // Post To Button with Dropdown
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp),
            contentAlignment = Alignment.Center
        ) {
            Button(
                onClick = { showPostToDropdown = !showPostToDropdown },
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF6CA6A3)),
                shape = RoundedCornerShape(8.dp),
                modifier = Modifier
                    .height(48.dp)
                    .width(180.dp)
            ) {
                Text(
                    text = if (careScheduleState.postTo.isBlank()) "POST TO" else "POST TO  ▼",
                    fontSize = 18.sp,
                    color = Color.White,
                    fontWeight = FontWeight.Medium
                )
            }

            DropdownMenu(
                expanded = showPostToDropdown,
                onDismissRequest = { showPostToDropdown = false },
                modifier = Modifier.width(180.dp)
            ) {
                DropdownMenuItem(
                    text = { Text("All") },
                    onClick = {
                        careScheduleViewModel.handleEvent(CareScheduleEvent.OnPostToChange("All"))
                        showPostToDropdown = false
                    }
                )
                careScheduleState.userList.forEach { user ->
                    DropdownMenuItem(
                        text = { Text(user) },
                        onClick = {
                            careScheduleViewModel.handleEvent(CareScheduleEvent.OnPostToChange(user))
                            showPostToDropdown = false
                        }
                    )
                }
            }
        }

        // Submit Button
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 32.dp),
            contentAlignment = Alignment.Center
        ) {
            Button(
                onClick = { showConfirmDialog = true },
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF6CA6A3)),
                shape = RoundedCornerShape(8.dp),
                modifier = Modifier
                    .height(48.dp)
                    .width(180.dp)
            ) {
                Text(
                    text = "Submit",
                    fontSize = 18.sp,
                    color = Color.White,
                    fontWeight = FontWeight.Bold
                )
            }
        }

        // Loading and error state at the bottom
        if (careScheduleState.isLoading) {
            Spacer(modifier = Modifier.height(24.dp))
            Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        }
        if (careScheduleState.error != null) {
            Spacer(modifier = Modifier.height(8.dp))
            Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                Text(
                    careScheduleState.error ?: "",
                    color = Color.Red,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}