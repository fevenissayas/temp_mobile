package com.example.elderlycare2.presentation.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.elderlycare2.R
import com.example.elderlycare2.data.remote.response.NurseListResponse
import com.example.elderlycare2.presentation.component.BottomNavBar
import com.example.elderlycare2.presentation.viewmodel.LoginViewModel
import com.example.elderlycare2.presentation.viewmodel.NurseListViewModel
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.elderlycare2.presentation.state.NurseListEvent
import java.net.URLEncoder

@Composable
fun NurseListScreen(
    navController: NavController,
    loginViewModel: LoginViewModel,
    nurseListViewModel: NurseListViewModel = hiltViewModel(),
    onProfileClick: (String) -> Unit = {},
    onElderClick: (NurseListResponse) -> Unit
) {
    val state by nurseListViewModel.state.collectAsState()
    var searchQuery by remember { mutableStateOf("") }


    LaunchedEffect(Unit) {
        nurseListViewModel.handleEvent(NurseListEvent.FetchElderList)
    }

    Scaffold(
        bottomBar = { BottomNavBar(navController = navController, loginViewModel = loginViewModel) }
    ) { padding ->
        Column(
            modifier = Modifier
                .background(Color.White)
                .verticalScroll(rememberScrollState())
                .padding(padding)
                .fillMaxSize()
                .padding(horizontal = 16.dp, vertical = 8.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(bottom = 16.dp)
            ) {
                Image(
                    painter = painterResource(id = R.drawable.logo),
                    contentDescription = null,
                    modifier = Modifier.size(80.dp)
                )
                Spacer(modifier = Modifier.width(16.dp))
                Text(
                    "List Of Elders",
                    fontSize = 28.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 24.dp)
            ) {
                OutlinedTextField(
                    value = searchQuery,
                    onValueChange = { searchQuery = it },
                    placeholder = { Text("Search by name or email", fontSize = 14.sp, color = Color.Gray) },
                    shape = RoundedCornerShape(16.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedContainerColor = Color(0xFFF2F3F4),
                        unfocusedContainerColor = Color(0xFFF2F3F4),
                        focusedBorderColor = Color(0xFFB2BABB),
                        unfocusedBorderColor = Color(0xFFB2BABB),
                        focusedPlaceholderColor = Color.Gray,
                        unfocusedPlaceholderColor = Color.Gray,
                    ),
                    modifier = Modifier
                        .weight(1f)
                        .height(48.dp)
                )
                Spacer(modifier = Modifier.width(12.dp))
                Icon(
                    imageVector = Icons.Filled.Search,
                    contentDescription = "Search",
                    modifier = Modifier
                        .size(30.dp)
                        .align(Alignment.CenterVertically)
                )
            }

            if (state.isLoading) {
                Text("Loading...", modifier = Modifier.align(Alignment.CenterHorizontally))
            } else if (state.error != null) {
                Text("Error: ${state.error}", color = Color.Red, modifier = Modifier.align(Alignment.CenterHorizontally))
            } else {
                val filteredList = state.elderList.filter {
                    it.name.contains(searchQuery, ignoreCase = true) ||
                            it.email.contains(searchQuery, ignoreCase = true)
                }
                filteredList.forEach { elder ->
                    ElderCard(elder = elder, onElderClick = { selectedElder ->
                        navController.navigate(
                            "nurse/user/${selectedElder.id}/${URLEncoder.encode(selectedElder.name, "UTF-8")}/${URLEncoder.encode(selectedElder.email, "UTF-8")}/details"
                        )
                    })
                }
                if (filteredList.isEmpty() && searchQuery.isNotEmpty()) {
                    Text("No results found.", color = Color.Gray, modifier = Modifier.align(Alignment.CenterHorizontally))
                }
            }
        }
    }
}

@Composable
fun ElderCard(
    elder: NurseListResponse,
    onElderClick: (NurseListResponse) -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(110.dp)
            .border(1.dp, Color(0xFFB2BABB), RoundedCornerShape(10.dp))
            .clickable { onElderClick(elder) },
        shape = RoundedCornerShape(10.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFE8F8F5))
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .padding(horizontal = 16.dp, vertical = 8.dp)
                .fillMaxSize()
        ) {
            Image(
                painter = painterResource(id = R.drawable.profile),
                contentDescription = null,
                modifier = Modifier.size(60.dp),
                contentScale = ContentScale.Crop
            )
            Spacer(modifier = Modifier.width(20.dp))
            Column(
                verticalArrangement = Arrangement.Center
            ) {
                Text(elder.name, style = MaterialTheme.typography.bodyLarge, fontWeight = FontWeight.SemiBold)
                Text(elder.email, style = MaterialTheme.typography.bodyMedium, color = Color.DarkGray)
            }
        }
    }
}