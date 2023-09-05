package com.tnmd.learningenglishapp.screens.list_words

data class WordsUiState(
    val currentdWord: String = "",
    val currentWordCount: Int = 1,
    val score: Int = 0,
    val isGuessedWordWrong: Boolean = false,
    val isGameOver: Boolean = false
)
