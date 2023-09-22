package com.tnmd.learningenglishapp.screens.extension

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.TextField
import androidx.compose.material3.Button
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
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.tnmd.learningenglishapp.R
import com.tnmd.learningenglishapp.model.Words
import com.tnmd.learningenglishapp.screens.list_words.AudioPlayer
import com.tnmd.learningenglishapp.screens.list_words.WordsViewModel

@Composable
fun SearchEngWords(
    modifier: Modifier = Modifier,
    viewModel: ExtensionViewModel = hiltViewModel()
) {
    val mediumPadding = dimensionResource(R.dimen.padding_medium)
    val uiState by viewModel.uiState.collectAsState()

    Row(modifier = modifier) {
        TextField(
            value = uiState.wordUserSearch,
            onValueChange = {
                viewModel.wordsSearchChange(it)
                // Xử lý khi giá trị nhập thay đổi (đã thêm dòng này)
            })
        Button(onClick = { viewModel.searchSubmit() }) {
            Text(text = "Tra từ")
        }
    }

    // Hiển thị thanh tiến trình nếu đang xử lý
    if (viewModel.isLoading.value) {
        CircularProgressIndicator(
            modifier = Modifier
                .size(32.dp)
                .padding(16.dp)  // Thêm khoảng cách từ vòng tròn xoay đến các thành phần khác
        )
    }

    Card(
        modifier = modifier
            .fillMaxWidth()
            .background(Color.Blue, shape = RoundedCornerShape(10.dp)),
        elevation = 5.dp
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(mediumPadding),
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.padding(mediumPadding)
        ) {

            Log.d("ExtensionViewModel1", uiState.hasWordSearch.toString() + " - " + uiState.wordSearchResult)
            if (uiState.hasWordSearch == true && uiState.wordSearchResult != null) {
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