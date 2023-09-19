package com.tnmd.learningenglishapp.screens.extension

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.semantics.SemanticsProperties.ImeAction
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import io.getstream.chat.android.ui.ChatUI.style

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun CheckGrammarScreen(viewModel: ExtensionViewModel) {
    val uiState by viewModel.uiState.collectAsState()
    val editResponse by viewModel.editResponse.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        BasicTextField(
            value = uiState.userStringGues,
            onValueChange = {
                viewModel.updateUserCheckGrammar(it)
            },
            keyboardOptions = KeyboardOptions.Default.copy(
                imeAction = androidx.compose.ui.text.input.ImeAction.Done
            ),
            keyboardActions = KeyboardActions(
                onDone = {
                    // Xử lý sự kiện khi nhấn "Done" ở đây, nếu cần
                }
            ),
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .background(Color.White)
                .border(1.dp, Color.Gray)
                .padding(8.dp)
                .focusRequester(FocusRequester())
        )

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = {
                viewModel.submitUserStringToSapling()
            },
            modifier = Modifier.align(Alignment.CenterHorizontally)
        ) {
            Text(text = "Submit")
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = {
                // Hiển thị hộp thoại lỗi khi nút này được bấm
                val errors = editResponse?.edits ?: emptyList()
                viewModel.showErrorsDialog(errors)
            },
            modifier = Modifier.align(Alignment.CenterHorizontally)
        ) {
            Text(text = "Số lượng lỗi: ${editResponse?.edits?.size ?: 0}")
        }

        // Hiển thị thanh tiến trình nếu đang xử lý
        if (viewModel.isLoading.value) {
            CircularProgressIndicator(
                modifier = Modifier
                    .size(32.dp)
                    .align(Alignment.CenterHorizontally)
            )
        }

        // Hộp thoại lỗi
        if (viewModel.showErrorsDialog.value) {
            AlertDialog(
                onDismissRequest = {
                    // Đóng hộp thoại lỗi khi người dùng chọn bất kỳ nơi nào ngoài hộp thoại
                    viewModel.closeErrorsDialog()
                },
                title = {
                    Text(text = "Danh sách lỗi")
                },
                text = {
                    // Hiển thị danh sách lỗi ở đây
                    val errors = viewModel.errorList
                    Column {
                        for (error in errors) {
                            val sentence = error.sentence.trim()
                            val annotatedText = buildAnnotatedString {
                                withStyle(
                                    style = SpanStyle(color = Color.Black)
                                ) {
                                    append("Câu lỗi: ")
                                    append(sentence.substring(0, error.start))
                                }
                                withStyle(
                                    style = SpanStyle(color = Color.Red)
                                ) {
                                    append(sentence.substring(error.start, error.end))
                                }
                                withStyle(
                                    style = SpanStyle(color = Color.Black)
                                ) {
                                    append(sentence.substring(error.end))
                                }
                            }

                            Text(
                                text =annotatedText,
                                modifier = Modifier.padding(4.dp)
                            )

                            Text(" Sửa lỗi: ${error.replacement}")
                            Spacer(modifier = Modifier.height(10.dp))
                        }
                    }
                },
                confirmButton = {
                    // Nút xác nhận để đóng hộp thoại lỗi
                    Button(
                        onClick = {
                            viewModel.closeErrorsDialog()
                        }
                    ) {
                        Text(text = "Đóng")
                    }
                }
            )
        }
    }
}