package com.tnmd.learningenglishapp.screens.chat

data class ScreenUiState(
    val isNextStep : Boolean = false,
    val username : String = "",
    val avatar : String = "",
    val email:  String ="",
    val level: String ="",
    val descriptionLevel: String = "",
    val validityPeriod: Int = 0
)
