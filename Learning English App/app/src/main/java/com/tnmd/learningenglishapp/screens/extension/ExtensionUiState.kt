package com.tnmd.learningenglishapp.screens.extension

import com.tnmd.learningenglishapp.model.Words
import org.checkerframework.framework.qual.EnsuresQualifier

data class ExtensionUiState(
    val currentPage : Int = 0,
    val userStringGues : String = "",
    val wordUserSearch : String = "",
    val hasWordSearch : Boolean = false,
    val wordSearchResult : Words = Words(),
    val isEditSchedule : Boolean = false,
    val dayUserChoosen : List<String> = emptyList<String>()
)
