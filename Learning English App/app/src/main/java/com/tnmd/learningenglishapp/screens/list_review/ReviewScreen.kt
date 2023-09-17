package com.tnmd.learningenglishapp.screens.list_review

import android.annotation.SuppressLint
import android.content.Context
import android.media.MediaPlayer
import android.util.Log
import android.widget.Button
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.tnmd.learningenglishapp.LIST_COURSES
import com.tnmd.learningenglishapp.LIST_REVIEW_SCREEN
import com.tnmd.learningenglishapp.R
import com.tnmd.learningenglishapp.model.Words
import com.tnmd.learningenglishapp.screens.list_words.ScoreDialog
import com.tnmd.learningenglishapp.screens.list_words.WordsViewModel
import org.checkerframework.checker.units.qual.Current
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

    Text(
        text = stringResource(R.string.current_words_count,gameUiState.currentWordCount-1),
        style = MaterialTheme.typography.titleLarge,
        modifier = Modifier
            .padding(bottom = mediumPadding, top = mediumPadding)
    )
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(mediumPadding),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {


        when (gameUiState.randomGame) {
            1 -> Game1(gameUiState.currentdWord, viewModel) // Trò chơi "Cho nghĩa của từ"
            2 -> Game2(gameUiState.currentdWord, viewModel) // Trò chơi "Cho nghe âm thanh của từ"
            3 -> Game3(gameUiState.currentdWord, viewModel) // Trò chơi "Đảo từ tiếng Anh"
        }

        if (gameUiState.isAnswered == true) {
            if (gameUiState.currentWordCount <= gameUiState.maxWordsOfCourse) {
                TextButton(
                    onClick = {
                        viewModel.nextQuestion()
                    },
                    modifier = Modifier.padding(top = mediumPadding)
                ) {
                    Text(text = stringResource(R.string.Continue))
                }
            } else {
                viewModel.updateScoreDialog()
                if (gameUiState.closeScoreDialog == false){
                    ScoreDialogReview(
                        score = gameUiState.score,
                        onPlayAgain = { viewModel.newGame() },
                        openScreen = openScreen,
                        viewModel = viewModel
                    )
                }
            }
        }

        Log.d(
            "checkMaxWords",
            gameUiState.maxWordsOfCourse.toString() + " - " + gameUiState.currentWordCount
        )
    }
    Log.d("CheckIsGuesWrongWithIsAnswered",gameUiState.isGuessedWordWrong.toString() +"- " +gameUiState.isAnswered )
    if((gameUiState.isGuessedWordWrong == false) && (gameUiState.isAnswered == true)){
        Box(
            modifier = Modifier
                .fillMaxSize()
        ) {
            // Nội dung chính của màn hình ở đây

            // Hiển thị hình ảnh bên dưới bên phải
            Image(
                painter = painterResource(id = R.drawable.correct),
                contentDescription = null,
                modifier = Modifier
                    .size(150.dp) // Điều chỉnh kích thước theo nhu cầu của bạn
                    .align(Alignment.BottomEnd) // Đặt ảnh bên dưới bên phải
            )
        }
        playSoundFromRaw(LocalContext.current, R.raw.sound_correct)
    }
    else if((gameUiState.isGuessedWordWrong == true) && (gameUiState.isAnswered == true)){
        Box(
            modifier = Modifier
                .fillMaxSize()
        ) {
            // Nội dung chính của màn hình ở đây

            // Hiển thị hình ảnh bên dưới bên phải
            Image(
                painter = painterResource(id = R.drawable.wrong),
                contentDescription = null,
                modifier = Modifier
                    .size(150.dp) // Điều chỉnh kích thước theo nhu cầu của bạn
                    .align(Alignment.BottomEnd) // Đặt ảnh bên dưới bên phải
            )
        }
        playSoundFromRaw(LocalContext.current, R.raw.sound_wrong)
    }
}

fun playSoundFromRaw(context: Context, resourceId: Int) {
    val mediaPlayer = MediaPlayer.create(context, resourceId)
    mediaPlayer.start()
}