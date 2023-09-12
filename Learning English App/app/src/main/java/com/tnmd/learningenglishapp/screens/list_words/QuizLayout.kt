package com.tnmd.learningenglishapp.screens.list_words

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.tnmd.learningenglishapp.model.Words
@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@OptIn(ExperimentalMaterialApi::class)
@Composable
fun QuizGame(words: List<Words>,
             openScreen: (String) -> Unit,
             viewModel: QuizViewModel = hiltViewModel()) {

    val uiState by viewModel.uiState.collectAsState()
    val totalQuestions = words.size // Số lượng câu hỏi bạn muốn
    var questionWord: Words by remember { mutableStateOf(viewModel.getNextWord(words, viewModel.usedWords)) }
    var answerOptions: List<Words> by remember { mutableStateOf(viewModel.generateAnswerOptions(words, questionWord)) }


    fun resetGame() {
        viewModel.usedWords.clear()
        questionWord = viewModel.getNextWord(words, viewModel.usedWords)
        answerOptions = viewModel.generateAnswerOptions(words, questionWord)
        viewModel.selectedOption = null
        viewModel.updateQuizUiStateAnswerOptions(false,0,false)
        viewModel.resetScore(0)
        viewModel.resetScoreDialog(false)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {

        Score(score = uiState.score, modifier = Modifier
            .align(Alignment.CenterHorizontally)
            .padding(20.dp))

        Spacer(modifier = Modifier.height(16.dp))

        Text(text = "What is the meaning of ${questionWord.name}?", style = MaterialTheme.typography.bodyLarge)

        Spacer(modifier = Modifier.height(16.dp))

        answerOptions.forEach { word ->
            AnswerOption(
                word = word,
                onOptionSelected = { selectedWord ->
                    if (!uiState.answered) {
                        if (selectedWord == questionWord) {
                            // Handle correct answer
                            viewModel.updateQuizUiStateAnswerOptions(true,20,true)
                        } else {
                            // Handle wrong answer
                            viewModel.selectedOption = selectedWord
                            viewModel.updateQuizUiStateAnswerOptions(false,0,true)
                        }
                    }
                },
                selected = viewModel.selectedOption == word,
                correct = uiState.correctAnswerSelected && word == questionWord
            )
            Spacer(modifier = Modifier.height(8.dp))
        }

        if (uiState.correctAnswerSelected) {
            Text(text = "Correct!", color = Color.Green)
        } else if (viewModel.selectedOption != null && uiState.answered) {
            Text(text = "Wrong! The correct answer is ${questionWord.means}", color = Color.Red)
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = {
                if (viewModel.usedWords.size < totalQuestions) {
                    questionWord = viewModel.getNextWord(words, viewModel.usedWords)
                    viewModel.usedWords.add(questionWord)
                    answerOptions = viewModel.generateAnswerOptions(words, questionWord)
                    viewModel.selectedOption = null
                    viewModel.updateQuizUiStateAnswerOptions(false,0,false)
                } else {
                    viewModel.resetScoreDialog(true)
                }
            },
            modifier = Modifier.align(Alignment.CenterHorizontally),
            enabled = uiState.answered && viewModel.usedWords.size <= totalQuestions // Enable the button when answered and not all questions are answered
        ) {
            Text(text = "Next")
        }
        // Hiển thị ScoreDialog nếu showScoreDialog là true
        if (uiState.showScoreDialog) {
            ScoreDialog(score = uiState.score, onPlayAgain = { resetGame() }, openScreen = openScreen)
        }
    }
}

@Composable
fun AnswerOption(word: Words, onOptionSelected: (Words) -> Unit, selected: Boolean, correct: Boolean) {
    val backgroundColor = when {
        selected && correct || correct -> Color.Green
        selected && !correct -> Color.Red
        else -> Color.Gray
    }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(50.dp)
            .background(backgroundColor)
            .clickable { onOptionSelected(word) },
        contentAlignment = Alignment.Center,
    ) {
        Text(text = word.means, color = Color.White)
    }
}


