package com.tnmd.learningenglishapp.screens.list_review

import android.annotation.SuppressLint
import android.util.Log
import android.widget.Button
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import com.tnmd.learningenglishapp.LIST_COURSES
import com.tnmd.learningenglishapp.LIST_REVIEW_SCREEN
import com.tnmd.learningenglishapp.R
import com.tnmd.learningenglishapp.model.Words
import com.tnmd.learningenglishapp.screens.list_words.ScoreDialog
import com.tnmd.learningenglishapp.screens.list_words.WordsViewModel
import kotlin.random.Random

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
@ExperimentalMaterialApi
fun ReviewScreen(
    openScreen: (String) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: ReviewViewModel = hiltViewModel()
) {
    val gameUiState by viewModel.uiState.collectAsState()
    val mediumPadding = dimensionResource(R.dimen.padding_medium)

    Column(
        modifier = Modifier
            .verticalScroll(rememberScrollState())
            .padding(mediumPadding),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Text(
            text = stringResource(R.string.app_name),
            style = MaterialTheme.typography.titleLarge,
        )
        if(gameUiState.currentdWord != null){
            val randomGame = getRandomGame()
            when (randomGame) {
                1 -> Game1(gameUiState.currentdWord, viewModel) // Trò chơi "Cho nghĩa của từ"
                2 -> Game2(gameUiState.currentdWord, viewModel) // Trò chơi "Cho nghe âm thanh của từ"
                3 -> Game3(gameUiState.currentdWord, viewModel) // Trò chơi "Đảo từ tiếng Anh"
            }
        }
        if(gameUiState.currentWordCount <= gameUiState.maxWordsOfCourse){
            TextButton(
                onClick = {
                    viewModel.nextQuestion(gameUiState.currentWordCount)
                }
            ) {
                Text(text = stringResource(R.string.Continue))
            }
        }else{
            TextButton(
                onClick = {

                }
            ) {
                Text(text = "Hoàn thành")
            }
        }
        Log.d("checkMaxWords", gameUiState.maxWordsOfCourse.toString() + " - " + gameUiState.currentWordCount)
    }


}

fun getRandomGame(): Int {
    return Random.nextInt(1, 4) // Chọn số nguyên ngẫu nhiên từ 1 đến 3
}
