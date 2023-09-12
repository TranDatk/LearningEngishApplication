package com.tnmd.learningenglishapp.screens.list_words

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.tnmd.learningenglishapp.model.Words
import com.tnmd.learningenglishapp.model.service.LogService
import com.tnmd.learningenglishapp.screens.LearningEnglishAppViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class QuizViewModel @Inject constructor(logService: LogService) : LearningEnglishAppViewModel(logService) {

    private val _uiState = MutableStateFlow(QuizUiState())
    val uiState: StateFlow<QuizUiState> = _uiState.asStateFlow()
    var usedWords: MutableSet<Words> = mutableSetOf()
    var selectedOption: Words? = null

    fun getNextWord(words: List<Words>, usedWords: Set<Words>): Words {
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

    fun updateQuizUiStateAnswerOptions(correctAnswerSelected : Boolean, score : Int, answered : Boolean){
        _uiState.update { currentState ->
            currentState.copy(
                correctAnswerSelected = correctAnswerSelected,
                score = currentState.score.plus(score),
                answered = answered
            )
        }
    }

    fun resetScoreDialog(showScoreDialog : Boolean){
        _uiState.update { currentState ->
            currentState.copy(
                showScoreDialog = showScoreDialog
            )
        }
    }

    fun resetScore(score : Int){
        _uiState.update { currentState ->
            currentState.copy(
                score = score
            )
        }
    }
}

