package com.tnmd.learningenglishapp.screens.extension

import android.annotation.SuppressLint
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.tnmd.learningenglishapp.screens.chatgpt.ChatGPTScreen
import dagger.hilt.android.lifecycle.HiltViewModel

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ExtensionScreen(viewModel : ExtensionViewModel = hiltViewModel()){
    val tabs = listOf("Kiểm tra ngữ pháp", "Tra từ", "Lịch học", "CHATGPT")
    val uiState by viewModel.uiState.collectAsState()
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(12.dp)
    ) {
        // Tab buttons
        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(tabs.size) { index ->
                TabButton(
                    text = tabs[index],
                    selected = uiState.currentPage == index,
                    onClick = { viewModel.changeTab(index) }
                )
            }
        }

        // Content for the selected tab
        when (uiState.currentPage) {
            0 -> {
                CheckGrammarScreen(viewModel = viewModel)
            }
            1 -> {

            }
            2 -> {
                // Content for Tab 3
            }
            3 -> {
                ChatGPTScreen(viewModel = viewModel)
            }
        }
    }
}
@Composable
fun TabButton(text: String, selected: Boolean, onClick: () -> Unit) {
    Button(
        onClick = onClick,
        modifier = Modifier.padding(10.dp)
            ,
        colors = ButtonDefaults.buttonColors(
            backgroundColor = if (selected) Color.Gray else Color.White
        )
    ) {
        Text(text = text, color = if (selected) Color.White else Color.Black)
    }
}

@Preview
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ExtensionScreenPreview(){
    ExtensionScreen()
}