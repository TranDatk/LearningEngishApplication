package com.tnmd.learningenglishapp.model.service

import com.tnmd.learningenglishapp.model.Words
import kotlinx.coroutines.flow.Flow

interface WordsService {

    val words: Flow<List<Words>>
    suspend fun getWords(wordsId: String): Words?
    suspend fun findAWordsWithName(wordsName : String) : Words?
}