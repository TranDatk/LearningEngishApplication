package com.tnmd.learningenglishapp.screens.list_review

import android.util.Log
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.tnmd.learningenglishapp.model.Words

@Composable
fun Game3(word: Words, viewModel: ReviewViewModel = hiltViewModel()) {
    val gameUiState by viewModel.uiState.collectAsState()

    // Đảo từ tiếng Anh và hiển thị lên màn hình
    val reversedWord = word.name.reversed()

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        elevation = 4.dp
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth()
        ) {
            Text(
                text = reversedWord,
                style = MaterialTheme.typography.h4,
                modifier = Modifier.padding(bottom = 16.dp).align(Alignment.CenterHorizontally)
            )

            // Người chơi nhập từ tiếng Anh
            OutlinedTextField(
                value = viewModel.userGuess,
                onValueChange = { viewModel.updateUserGuess(it) },
                readOnly = gameUiState.isAnswered,
                label = { Text("Nhập từ tiếng Anh sau khi đảo") }
            )

            // Xử lý khi người chơi nhấn kiểm tra
            Button(
                onClick = {
                    if (viewModel.userGuess.trim().equals(word.name, ignoreCase = true)) {
                        viewModel.checkUserGuess(true)
                        Log.d("checkUserGuess", "true")
                    } else {
                        viewModel.checkUserGuess(false)
                        Log.d("checkUserGuess", "false")
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp)
            ) {
                Text("Kiểm tra")
            }
        }
    }
}





