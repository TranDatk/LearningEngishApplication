package com.tnmd.learningenglishapp.screens.chatgpt

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Send
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.tnmd.learningenglishapp.screens.extension.ExtensionViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChatGPTScreen(viewModel: ExtensionViewModel = hiltViewModel()) {
    Column(modifier = Modifier.fillMaxSize()) {
        LazyColumn(
            modifier = Modifier
                .weight(1f)
                .padding(16.dp)
                .fillMaxHeight(),
            reverseLayout = true
        ) {
            items(viewModel.messages.reversed()) { message ->
                if (message.isUser) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 8.dp),
                        Arrangement.Start
                    ) {
                        MessageBubble(message.content)
                        Spacer(modifier = Modifier.height(6.dp))
                    }
                } else {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 8.dp),
                        Arrangement.End
                    ) {
                        MessageBubble(message.content)
                        Spacer(modifier = Modifier.height(6.dp))
                    }
                }
                Spacer(modifier = Modifier.height(6.dp))
            }
        }


        // Hiển thị thông báo "Đang chờ phản hồi" nếu cần
        if (viewModel.isWaitingForResponse.value) {
                Text(
                    text = "Đang chờ phản hồi...",
                    modifier = Modifier
                        .padding(16.dp)
                        .fillMaxWidth(),
                    textAlign = TextAlign.Center,
                    color = Color.Black
                )
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            var inputText by remember { mutableStateOf("") }
            TextField(
                value = inputText,
                onValueChange = { inputText = it },
                modifier = Modifier.weight(1f),
                colors = TextFieldDefaults.textFieldColors(),
                label = { Text("Nhập tin nhắn") },
                singleLine = true,
                keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Send),
                keyboardActions = KeyboardActions(onSend = {
                    viewModel.sendMessage(inputText)
                    inputText = ""
                })
            )

            IconButton(
                onClick = {
                    viewModel.sendMessage(inputText)
                    inputText = ""
                    viewModel.isWaitingForResponse.value = true
                },
                modifier = Modifier.padding(start = 8.dp)
            ) {
                Icon(Icons.Default.Send, contentDescription = "Send message")
            }
        }
    }
}


@Composable
fun MessageBubble(text: String) {
    Surface(
        shape = MaterialTheme.shapes.medium,
        tonalElevation = 4.dp,
    ) {
        Text(
            text = text,
            modifier = Modifier
                .padding(16.dp),
            fontSize = 16.sp
        )
    }
}