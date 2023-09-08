package com.tnmd.learningenglishapp.screens.list_words

import com.tnmd.learningenglishapp.model.Words

data class WordsUiState(
    val currentdWord: Words = Words("","","","",""),
    val currentWordCount: Int = 1,
    val score: Int = 0,
    val isGuessedWordWrong: Boolean = false,
    val isGameOver: Boolean = false,
    val maxWordsOfCourse : Int = 1
)
