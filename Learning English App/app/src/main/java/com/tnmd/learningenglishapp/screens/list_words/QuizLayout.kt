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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.tnmd.learningenglishapp.model.Words
@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@OptIn(ExperimentalMaterialApi::class)
@Composable
fun QuizGame(words: List<Words>, openScreen: (String) -> Unit) {
    var usedWords: MutableSet<Words> by remember { mutableStateOf(mutableSetOf()) }
    var questionWord: Words by remember { mutableStateOf(getNextWord(words, usedWords)) }
    var answerOptions: List<Words> by remember { mutableStateOf(generateAnswerOptions(words, questionWord)) }
    var selectedOption: Words? by remember { mutableStateOf(null) }
    var correctAnswerSelected: Boolean by remember { mutableStateOf(false) }
    var answered: Boolean by remember { mutableStateOf(false) }
    var score: Int by remember { mutableStateOf(0) }
    val totalQuestions = words.size // Số lượng câu hỏi bạn muốn
    // Biến để kiểm soát việc hiển thị ScoreDialog
    val showScoreDialog = remember { mutableStateOf(false) }

    fun resetGame() {
        usedWords.clear()
        questionWord = getNextWord(words, usedWords)
        answerOptions = generateAnswerOptions(words, questionWord)
        selectedOption = null
        correctAnswerSelected = false
        answered = false
        score = 0 // Đặt lại điểm số khi chơi lại
        // Đặt showScoreDialog thành false để ẩn ScoreDialog
        showScoreDialog.value = false
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {

        Score(score = score, modifier = Modifier
            .align(Alignment.CenterHorizontally)
            .padding(20.dp))

        Spacer(modifier = Modifier.height(16.dp))

        Text(text = "What is the meaning of ${questionWord.name}?", style = MaterialTheme.typography.bodyLarge)

        Spacer(modifier = Modifier.height(16.dp))

        answerOptions.forEach { word ->
            AnswerOption(
                word = word,
                onOptionSelected = { selectedWord ->
                    if (!answered) {
                        if (selectedWord == questionWord) {
                            // Handle correct answer
                            correctAnswerSelected = true
                            score += 20 // Cộng điểm khi trả lời đúng
                        } else {
                            // Handle wrong answer
                            selectedOption = selectedWord
                            correctAnswerSelected = false
                        }

                        answered = true
                    }
                },
                selected = selectedOption == word,
                correct = correctAnswerSelected && word == questionWord
            )
            Spacer(modifier = Modifier.height(8.dp))
        }

        if (correctAnswerSelected) {
            Text(text = "Correct!", color = Color.Green)
        } else if (selectedOption != null && answered) {
            Text(text = "Wrong! The correct answer is ${questionWord.means}", color = Color.Red)
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = {
                if (usedWords.size < totalQuestions) {
                    questionWord = getNextWord(words, usedWords)
                    usedWords.add(questionWord)
                    answerOptions = generateAnswerOptions(words, questionWord)
                    selectedOption = null
                    correctAnswerSelected = false
                    answered = false
                } else {
                    showScoreDialog.value = true
                }
            },
            modifier = Modifier.align(Alignment.CenterHorizontally),
            enabled = answered && usedWords.size <= totalQuestions // Enable the button when answered and not all questions are answered
        ) {
            Text(text = "Next")
        }
        // Hiển thị ScoreDialog nếu showScoreDialog là true
        if (showScoreDialog.value) {
            ScoreDialog(score = score, onPlayAgain = { resetGame() }, openScreen = openScreen)
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

fun getNextWord(words: List<Words>, usedWords: MutableSet<Words>): Words {
    val unusedWords = words.filterNot { it in usedWords }
    return if (unusedWords.isNotEmpty()) {
        unusedWords.random()
    } else {
        words.random()
    }
}

fun generateAnswerOptions(words: List<Words>, questionWord: Words): List<Words> {
    val shuffledWords = words.shuffled()
    val answerOptions = mutableListOf<Words>()

    // Add the correct answer option (meaning of the question word)
    answerOptions.add(questionWord)

    // Add 3 random incorrect answer options
    for (word in shuffledWords) {
        if (word != questionWord && answerOptions.size < 4) {
            answerOptions.add(word)
        }
    }
    return answerOptions.shuffled()
}
