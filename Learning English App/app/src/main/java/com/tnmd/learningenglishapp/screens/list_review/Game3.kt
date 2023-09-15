package com.tnmd.learningenglishapp.screens.list_review

import android.util.Log
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.hilt.navigation.compose.hiltViewModel
import com.tnmd.learningenglishapp.model.Words

@Composable
fun Game3(word: Words, viewModel: ReviewViewModel = hiltViewModel()) {
    val gameUiState by viewModel.uiState.collectAsState()

    // Đảo từ tiếng Anh và hiển thị lên màn hình
    val reversedWord = word.name.reversed()
    Text(text = reversedWord)

    // Người chơi nhập từ tiếng Anh

    OutlinedTextField(
        value = viewModel.userGuess,
        onValueChange = { viewModel.updateUserGuess(it) },
        label = { Text("Nhập từ tiếng Anh sau khi đảo") }
    )

    // Xử lý khi người chơi nhấn kiểm tra
    Button(
        onClick = {
            if (viewModel.userGuess.trim().equals(word.name, ignoreCase = true)) {
                viewModel.checkUserGuess(true)
                Log.d("checkUserGues", "true")
            } else {
                viewModel.checkUserGuess(false)
                Log.d("checkUserGues", "false")
            }
        }
    ) {
        Text("Kiểm tra")
    }
}