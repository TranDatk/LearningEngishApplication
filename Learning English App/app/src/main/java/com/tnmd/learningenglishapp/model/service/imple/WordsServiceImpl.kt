package com.tnmd.learningenglishapp.model.service.imple

import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.dataObjects
import com.google.firebase.firestore.ktx.toObject
import com.tnmd.learningenglishapp.model.Words
import com.tnmd.learningenglishapp.model.service.WordsService
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class WordsServiceImpl @Inject constructor(private val firestore: FirebaseFirestore
) : WordsService {

    override val words: Flow<List<Words>>
        get() = firestore.collection(WORDS_COLLECTION).dataObjects()


    override suspend fun getWords(wordsId: String): Words? =
        firestore.collection(WORDS_COLLECTION).document(wordsId).get().await().toObject()

    companion object {
        private const val WORDS_COLLECTION = "courses"
    }
}