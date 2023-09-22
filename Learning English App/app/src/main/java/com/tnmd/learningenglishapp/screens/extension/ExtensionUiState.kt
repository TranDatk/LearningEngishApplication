package com.tnmd.learningenglishapp.screens.extension

import com.tnmd.learningenglishapp.model.Words

data class ExtensionUiState(
    val currentPage : Int = 0,
    val userStringGues : String = "",
    val wordUserSearch : String = "",
    val hasWordSearch : Boolean = false,
    val wordSearchResult : Words = Words()
)
