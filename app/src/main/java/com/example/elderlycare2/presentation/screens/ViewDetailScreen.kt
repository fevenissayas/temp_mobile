import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.material3.ButtonDefaults.buttonColors
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.elderlycare2.R
import com.example.elderlycare2.presentation.component.BottomNavBar
import com.example.elderlycare2.presentation.state.ViewDetailEvent
import com.example.elderlycare2.presentation.viewmodel.LoginViewModel
import com.example.elderlycare2.presentation.viewmodel.ViewDetailViewModel
import com.example.elderlycare2.presentation.viewmodel.NurseDeleteViewModel

@Composable
fun ViewDetailScreen(
    navController: NavController,
    elderId: String,
    elderName: String,
    elderEmail: String,
    viewModel: ViewDetailViewModel = hiltViewModel(),
    nurseDeleteViewModel: NurseDeleteViewModel = hiltViewModel(),
    loginViewModel: LoginViewModel
) {
    val state by viewModel.viewDetailState.collectAsState()
    val deleteState by nurseDeleteViewModel.nurseDeleteState.collectAsState()

    var showHeartRateDialog by remember { mutableStateOf(false) }
    var showBloodPressureDialog by remember { mutableStateOf(false) }
    var showSugarLevelDialog by remember { mutableStateOf(false) }
    var showDeleteDialog by remember { mutableStateOf(false) }

    LaunchedEffect(elderId) {
        viewModel.handleEvent(ViewDetailEvent.FetchElderDetail(elderId))
    }

    // Handle delete success/effect
    LaunchedEffect(deleteState.successMessage) {
        if (deleteState.successMessage != null) {
            // Navigate back after delete success
            navController.popBackStack()
        }
    }

    Scaffold(
        bottomBar = { BottomNavBar(navController = navController, loginViewModel = loginViewModel) }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .background(Color.White)
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(paddingValues)
                .padding(start = 20.dp, end = 20.dp, top = 35.dp, bottom = 15.dp)
        ) {
            // Top bar
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                IconButton(onClick = { navController.popBackStack() }) {
                    Icon(
                        imageVector = Icons.Filled.ArrowBack,
                        contentDescription = "Back"
                    )
                }
                Text(
                    text = "Visit details",
                    style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold)
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Profile section
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                Image(
                    painter = painterResource(id = R.drawable.logo),
                    contentDescription = "Profile Image",
                    modifier = Modifier.size(90.dp),
                    contentScale = ContentScale.Crop
                )
                Spacer(modifier = Modifier.width(30.dp))
                Column(modifier = Modifier.fillMaxWidth()) {
                    Text(elderName, fontWeight = FontWeight.SemiBold)
                    Text(elderEmail)
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Caretaker Info Box
            Column(
                verticalArrangement = Arrangement.spacedBy(16.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color(0xFFD9F2F1), shape = RoundedCornerShape(12.dp))
                    .padding(12.dp)
            ) {
                Text("Caretaker: ${state.careTaker}", fontWeight = FontWeight.Bold)
                Text("ID: ${viewModel.userId}", fontWeight = FontWeight.Bold)
                Text("Nurse: ${viewModel.nurseName}", fontWeight = FontWeight.Bold)
                Text("Blood Type: ${state.bloodType}", fontWeight = FontWeight.Bold)
            }

            Spacer(modifier = Modifier.height(20.dp))

            // Diagnosis
            Text("Diagnosis", style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold))
            Spacer(modifier = Modifier.height(12.dp))

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.White)
                    .border(1.dp, Color.Gray, RoundedCornerShape(30.dp))
                    .shadow(
                        elevation = 1.dp,
                        shape = RoundedCornerShape(30.dp),
                        clip = false
                    ),
            ) {
                Column(
                    verticalArrangement = Arrangement.spacedBy(16.dp),
                    modifier = Modifier.padding(16.dp)
                ) {
                    Text(state.description, fontWeight = FontWeight.Bold)
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                VitalCard(
                    title = "Heart Rate",
                    value = "${if(state.heartRate.isNullOrBlank()) "70" else state.heartRate} BPM",
                    imageRes = R.drawable.heart_rate,
                    onUpdateClick = { showHeartRateDialog = true }
                )
                VitalCard(
                    title = "Blood Pressure",
                    value = "${if(state.bloodPressure.isNullOrBlank()) "120/80" else state.bloodPressure} MMHG",
                    imageRes = R.drawable.blood_pressure,
                    onUpdateClick = { showBloodPressureDialog = true }
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            Row(
                horizontalArrangement = Arrangement.Start,
                modifier = Modifier.fillMaxWidth()
            ) {
                VitalCard(
                    title = "Sugar Level",
                    value = "${if(state.sugarLevel.isNullOrBlank()) "73" else state.sugarLevel} mg/dL",
                    imageRes = R.drawable.sugar_level,
                    onUpdateClick = { showSugarLevelDialog = true }
                )
            }

            Spacer(modifier = Modifier.height(30.dp))

            // DELETE Button (nurse only)
            Button(
                onClick = { showDeleteDialog = true },
                colors = buttonColors(containerColor = Color(0xFFF44336)), // Red color
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp)
            ) {
                Text("Delete User", color = Color.White, fontWeight = FontWeight.Bold)
            }

            // Show error/success/loading message after delete
            if (deleteState.isLoading) {
                CircularProgressIndicator(
                    modifier = Modifier
                        .align(Alignment.CenterHorizontally)
                        .padding(top = 8.dp)
                )
            }
            deleteState.errorMessage?.let { error ->
                Text(
                    text = error,
                    color = Color.Red,
                    modifier = Modifier
                        .align(Alignment.CenterHorizontally)
                        .padding(top = 8.dp)
                )
            }
            deleteState.successMessage?.let { success ->
                Text(
                    text = success,
                    color = Color.Green,
                    modifier = Modifier
                        .align(Alignment.CenterHorizontally)
                        .padding(top = 8.dp)
                )
            }
        }

        // Dialogs for updating values
        if (showHeartRateDialog) {
            UpdateVitalDialog(
                title = "Update Heart Rate",
                initialValue = state.heartRate,
                unit = "BPM",
                onDismiss = { showHeartRateDialog = false },
                onUpdate = { newValue ->
                    viewModel.handleEvent(ViewDetailEvent.OnHeartRateChange(newValue))
                    viewModel.handleEvent(
                        ViewDetailEvent.UpdateUserDetail(
                            elderId = elderId,
                            heartRate = newValue,
                            bloodPressure = state.bloodPressure,
                            sugarLevel = state.sugarLevel
                        )
                    )
                    showHeartRateDialog = false
                }
            )
        }
        if (showBloodPressureDialog) {
            UpdateVitalDialog(
                title = "Update Blood Pressure",
                initialValue = state.bloodPressure,
                unit = "MMHG",
                onDismiss = { showBloodPressureDialog = false },
                onUpdate = { newValue ->
                    viewModel.handleEvent(ViewDetailEvent.OnBloodPressureChange(newValue))
                    viewModel.handleEvent(
                        ViewDetailEvent.UpdateUserDetail(
                            elderId = elderId,
                            heartRate = state.heartRate,
                            bloodPressure = newValue,
                            sugarLevel = state.sugarLevel
                        )
                    )
                    showBloodPressureDialog = false
                }
            )
        }
        if (showSugarLevelDialog) {
            UpdateVitalDialog(
                title = "Update Sugar Level",
                initialValue = state.sugarLevel,
                unit = "mg/dL",
                onDismiss = { showSugarLevelDialog = false },
                onUpdate = { newValue ->
                    viewModel.handleEvent(ViewDetailEvent.OnSugarLevelChange(newValue))
                    viewModel.handleEvent(
                        ViewDetailEvent.UpdateUserDetail(
                            elderId = elderId,
                            heartRate = state.heartRate,
                            bloodPressure = state.bloodPressure,
                            sugarLevel = newValue
                        )
                    )
                    showSugarLevelDialog = false
                }
            )
        }

        // DELETE confirmation dialog
        if (showDeleteDialog) {
            AlertDialog(
                onDismissRequest = { showDeleteDialog = false },
                confirmButton = {
                    Button(
                        onClick = {
                            nurseDeleteViewModel.handleEvent(
                                com.example.elderlycare2.presentation.state.NurseDeleteEvent.DeleteUser(elderId)
                            )
                            showDeleteDialog = false
                        },
                        colors = buttonColors(containerColor = Color(0xFFF44336))
                    ) {
                        Text("Delete", color = Color.White)
                    }
                },
                dismissButton = {
                    TextButton(onClick = { showDeleteDialog = false }) {
                        Text("Cancel")
                    }
                },
                title = { Text("Delete User") },
                text = { Text("Are you sure you want to delete this user? This action cannot be undone.") },
                shape = RoundedCornerShape(16.dp)
            )
        }
    }
}

@Composable
fun VitalCard(
    title: String,
    value: String,
    imageRes: Int,
    onUpdateClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .width(160.dp)
            .height(160.dp)
            .border(1.dp, Color.Gray, RoundedCornerShape(30.dp))
            .shadow(
                elevation = 8.dp,
                shape = RoundedCornerShape(30.dp),
                clip = false
            ),
        shape = RoundedCornerShape(15.dp),
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(color = Color.White),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                modifier = Modifier.padding(start = 13.dp, top = 20.dp, end = 13.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Image(
                    painter = painterResource(id = imageRes),
                    contentDescription = title,
                    modifier = Modifier
                        .size(50.dp)
                        .clip(RoundedCornerShape(10.dp))
                )
                Text(
                    title,
                    fontWeight = FontWeight.SemiBold,
                    modifier = Modifier
                        .padding(start = 12.dp)
                        .align(Alignment.CenterVertically)
                )
            }

            Text(
                value,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.align(Alignment.CenterHorizontally)
            )
            Spacer(modifier = Modifier.height(10.dp))
            Button(
                onClick = onUpdateClick,
                contentPadding = PaddingValues(horizontal = 8.dp, vertical = 0.dp),
                modifier = Modifier
                    .padding(end = 20.dp, bottom = 6.dp)
                    .height(20.dp)
                    .align(Alignment.End),
                colors = buttonColors(containerColor = Color(0xFFD9F2F1))
            ) {
                Text("Update", fontSize = 8.sp, modifier = Modifier.fillMaxHeight(), color = Color.Black)
            }
        }
    }
}

@Composable
fun UpdateVitalDialog(
    title: String,
    initialValue: String,
    unit: String,
    onDismiss: () -> Unit,
    onUpdate: (String) -> Unit
) {
    var text by remember { mutableStateOf(initialValue) }
    AlertDialog(
        onDismissRequest = onDismiss,
        confirmButton = {
            Button(
                onClick = { onUpdate(text) },
                enabled = text.isNotBlank()
            ) {
                Text("Update")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel")
            }
        },
        title = { Text(title) },
        text = {
            OutlinedTextField(
                value = text,
                onValueChange = { text = it },
                label = { Text("Enter $title") },
                trailingIcon = { Text(unit) },
                singleLine = true,
                keyboardOptions = KeyboardOptions(
                    keyboardType = if (unit == "mg/dL" || unit == "BPM") KeyboardType.Number else KeyboardType.Text
                )
            )
        },
        shape = RoundedCornerShape(16.dp)
    )
}