package com.tnmd.learningenglishapp.screens.list_review

import com.tnmd.learningenglishapp.model.Words

data class ReviewUiState(
    val currentdWord: Words = Words("","","","",""),
    val currentWordCount: Int = 1,
    val score: Int = 0,
    val isGuessedWordWrong: Boolean = false,
    val isGameOver: Boolean = false,
    val maxWordsOfCourse : Int = 1,
    val isAnswered : Boolean = false,
    val randomGame : Int = 1,
    val closeScoreDialog : Boolean = true,
    val isPlayed : Boolean = false
)
