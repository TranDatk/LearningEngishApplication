package com.tnmd.learningenglishapp.screens.list_words

import android.util.Log
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
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
class WordsViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val words_coursesService: Words_CoursesService,
    private val scoresService: ScoresService,
    private val processesService: ProcessesService,
    logService: LogService
) : LearningEnglishAppViewModel(logService){

    val SCORE_INCREASE = 20
    var words = mutableStateListOf<Words>()
    private val _uiState = MutableStateFlow(WordsUiState())
    val uiState: StateFlow<WordsUiState> = _uiState.asStateFlow()

    // Set of words used in the game
    private var usedWords: MutableSet<Words> = mutableSetOf()
    private lateinit var currentWord: Words
    private val score = mutableStateOf(Scores())
    private val process = mutableStateOf(Processes())

    init {
        val coursesId = savedStateHandle.get<String>(COURSES_ID)
        if (coursesId != null) {
            viewModelScope.launch {
                val wordsList =
                    words_coursesService.getWordsFromCourses(coursesId.idFromParameter())
                words.clear() // Xóa tất cả các phần tử hiện có trong mutableStateListOf<Words>
                words.addAll(wordsList.filterNotNull()) // Thêm danh sách từ wordsList sau khi loại bỏ các phần tử null
                resetGame()

                scoresService.newOrUpdateScore(score.value.copy(coursesId = coursesId.idFromParameter()))
                score.value = scoresService.getScoreByCoursesId(coursesId.idFromParameter())!!
            }
            launchCatching {
                processesService.newAndUpdateProcessesByLearn(process.value.copy(coursesId = coursesId.idFromParameter()))
                Log.d("checkCoursesId", process.toString())
                process.value = processesService.getProcessesByCoursesId(coursesId.idFromParameter())!!
            }

        }
    }

    /*
     * Re-initializes the game data to restart the game.
     */
    fun resetGame() {
        usedWords.clear()
        _uiState.value = WordsUiState(currentdWord = pickRandomWord(), maxWordsOfCourse = words.size)
    }


    /*
     * Skip to next word
     */
    fun skipWord(currentWordCount : Int) {
        updateGameState(_uiState.value.score.plus(SCORE_INCREASE))
        launchCatching {
            processesService.updateProcessesLearn(
            process.value.copy(processesLearn = process.value.processesLearn + currentWordCount/words.size.toDouble()))
        }
    }

    /*
     * Picks a new currentWord and currentScrambledWord and updates UiState according to
     * current game state.
     */
    private fun updateGameState(updatedScore: Int) {
        if (usedWords.size == words.size){
            //Last round in the game, update isGameOver to true, don't pick a new word
            _uiState.update { currentState ->
                currentState.copy(
                    isGuessedWordWrong = false,
                    currentWordCount = currentState.currentWordCount.inc(),
                    score = updatedScore
                )
            }
        } else{
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

    fun updateScoreToFireStore(scores : Int){
        score.value = score.value.copy(score = score.value.score + scores)
        launchCatching {
            scoresService.updateScore(score.value)
        }
    }
}