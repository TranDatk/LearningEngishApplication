package com.tnmd.learningenglishapp.screens.list_words

import com.tnmd.learningenglishapp.model.Words

data class QuizUiState(
    val correctAnswerSelected: Boolean = false,
    val answered: Boolean = false,
    val score : Int = 0,
    val showScoreDialog : Boolean = false
)
