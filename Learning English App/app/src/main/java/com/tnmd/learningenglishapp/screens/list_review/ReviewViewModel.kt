package com.tnmd.learningenglishapp.screens.list_review

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.tnmd.learningenglishapp.COURSES_ID
import com.tnmd.learningenglishapp.common.ext.idFromParameter
import com.tnmd.learningenglishapp.model.Processes
import com.tnmd.learningenglishapp.model.Scores
import com.tnmd.learningenglishapp.model.Words
import com.tnmd.learningenglishapp.model.service.LogService
import com.tnmd.learningenglishapp.model.service.ProcessesService
import com.tnmd.learningenglishapp.model.service.ScoresService
import com.tnmd.learningenglishapp.model.service.Words_CoursesService
import com.tnmd.learningenglishapp.screens.LearningEnglishAppViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ReviewViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val words_coursesService: Words_CoursesService,
    private val scoresService: ScoresService,
    private val processesService: ProcessesService,
    logService: LogService
) : LearningEnglishAppViewModel(logService) {
    val SCORE_INCREASE = 20
    var words = mutableStateListOf<Words>()
    private val _uiState = MutableStateFlow(ReviewUiState())
    val uiState: StateFlow<ReviewUiState> = _uiState.asStateFlow()

    // Set of words used in the game
    private var usedWords: MutableSet<Words> = mutableSetOf()
    private lateinit var currentWord: Words
    private val score = mutableStateOf(Scores())
    private val process = mutableStateOf(Processes())
    var userGuess by mutableStateOf("")
        private set

    init {
        val coursesId = savedStateHandle.get<String>(COURSES_ID)
        if (coursesId != null) {
            viewModelScope.launch {
                val wordsList =
                    words_coursesService.getWordsFromCourses(coursesId.idFromParameter())
                words.clear() // Xóa tất cả các phần tử hiện có trong mutableStateListOf<Words>
                words.addAll(wordsList.filterNotNull()) // Thêm danh sách từ wordsList sau khi loại bỏ các phần tử null
                newGame()

                scoresService.newOrUpdateScore(score.value.copy(coursesId = coursesId.idFromParameter()))
                score.value = scoresService.getScoreByCoursesId(coursesId.idFromParameter())!!
            }
            launchCatching {
                processesService.newAndUpdateProcessesByReview(process.value.copy(coursesId = coursesId.idFromParameter()))
                Log.d("checkCoursesId", process.toString())
                process.value =
                    processesService.getProcessesByCoursesId(coursesId.idFromParameter())!!
            }

        }
    }

    fun newGame() {
        usedWords.clear()
        _uiState.value =
            ReviewUiState(currentdWord = pickRandomWord(), maxWordsOfCourse = words.size)
    }

    private fun pickRandomWord(): Words {
        // Continue picking up a new random word until you get one that hasn't been used before
        currentWord = words.toList().random()
        return if (usedWords.contains(currentWord)) {
            pickRandomWord()
        } else {
            usedWords.add(currentWord)
            return currentWord
        }
    }

    fun nextQuestion(currentWordCount: Int) {
        if (_uiState.value.isGuessedWordWrong == false) {
            updateGameState(_uiState.value.score.plus(SCORE_INCREASE))
            launchCatching {
                processesService.updateProcessesLearn(
                    process.value.copy(processesCheck = process.value.processesCheck + currentWordCount / words.size.toDouble())
                )
            }
        } else {
            updateGameState(_uiState.value.score.plus(0))
        }
    }

    private fun updateGameState(updatedScore: Int) {
        if (usedWords.size == words.size) {
            //Last round in the game, update isGameOver to true, don't pick a new word
            _uiState.update { currentState ->
                currentState.copy(
                    isGuessedWordWrong = false,
                    currentWordCount = currentState.currentWordCount.inc(),
                    score = updatedScore
                )
            }
        } else {
            // Normal round in the game
            _uiState.update { currentState ->
                currentState.copy(
                    isGuessedWordWrong = false,
                    currentdWord = pickRandomWord(),
                    currentWordCount = currentState.currentWordCount.inc(),
                    score = updatedScore
                )
            }
        }
    }

    fun checkUserGuess(isCorrect: Boolean) {
        if (isCorrect == false) {
            _uiState.update { currentState ->
                currentState.copy(isGuessedWordWrong = true)
            }
        } else {
            _uiState.update { currentState ->
                currentState.copy(isGuessedWordWrong = false)
            }
        }
    }
    fun updateUserGuess(guessedWord: String) {
        userGuess = guessedWord
    }
}