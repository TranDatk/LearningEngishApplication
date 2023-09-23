package com.tnmd.learningenglishapp.screens.extension

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.TextField
import androidx.compose.material.Button
import androidx.compose.material.ButtonColors
import androidx.compose.material.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.hilt.navigation.compose.hiltViewModel
import com.tnmd.learningenglishapp.R
import com.tnmd.learningenglishapp.model.Words
import com.tnmd.learningenglishapp.screens.list_words.AudioPlayer

@Composable
fun SearchEngWords(
    modifier: Modifier = Modifier,
    viewModel: ExtensionViewModel = hiltViewModel()
) {
    val mediumPadding = dimensionResource(R.dimen.padding_medium)
    val uiState by viewModel.uiState.collectAsState()

    Column(modifier = modifier.padding(top = 20.dp)) {
        // Row cho ô tìm kiếm
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = mediumPadding, vertical = 20.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            OutlinedTextField(
                modifier = Modifier
                    .weight(1f)
                    .padding(end = mediumPadding),
                value = uiState.wordUserSearch,
                onValueChange = {
                    viewModel.wordsSearchChange(it)
                    // Xử lý khi giá trị nhập thay đổi (đã thêm dòng này)
                }
            )
            Button(
                colors = ButtonDefaults.buttonColors(
                    backgroundColor = Color(ContextCompat.getColor(LocalContext.current, R.color.medium_orange))),
                onClick = { viewModel.searchSubmit() },
                modifier = Modifier.padding(start = mediumPadding)
            ) {
                Text(text = "Tra từ", color = Color(ContextCompat.getColor(LocalContext.current, R.color.white)))
            }
        }

        // Hiển thị thanh tiến trình nếu đang xử lý
        if (viewModel.isLoading.value) {
            CircularProgressIndicator(
                modifier = Modifier
                    .size(32.dp)
                    .align(Alignment.CenterHorizontally)
                    .padding(top = mediumPadding)
            )
        }
        
        Spacer(modifier = Modifier.padding(30.dp))

        // Hiển thị kết quả tìm kiếm
        if (uiState.hasWordSearch == true && uiState.wordSearchResult != null) {
            Card(
                modifier = Modifier
                    .background(
                        Color(
                            ContextCompat.getColor(
                                LocalContext.current,
                                R.color.medium_orange
                            )
                        ), shape = RoundedCornerShape(1.dp)
                    )
                    .padding(mediumPadding),
                elevation = 2.dp
            ) {
                Column(
                    verticalArrangement = Arrangement.spacedBy(mediumPadding),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.padding(mediumPadding)
                ) {
                    Log.d(
                        "ExtensionViewModel1",
                        uiState.hasWordSearch.toString() + " - " + uiState.wordSearchResult
                    )

                    Text(
                        text = uiState.wordSearchResult.name,
                        style = MaterialTheme.typography.displayMedium
                    )

                    Text(
                        text = uiState.wordSearchResult.pronounce,
                        style = MaterialTheme.typography.bodyLarge
                    )

                    Text(
                        text = uiState.wordSearchResult.means,
                        style = MaterialTheme.typography.bodyLarge
                    )

                    AudioPlayer(uiState.wordSearchResult.audioURL)

                    Text(
                        text = stringResource(R.string.Example, uiState.wordSearchResult.example),
                        textAlign = TextAlign.Center,
                        style = MaterialTheme.typography.titleSmall
                    )
                }
            }
        }
    }
}