package com.tnmd.learningenglishapp.model.service.imple

import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.dataObjects
import com.google.firebase.firestore.ktx.toObject
import com.tnmd.learningenglishapp.model.Words
import com.tnmd.learningenglishapp.model.service.WordsService
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class WordsServiceImpl @Inject constructor(private val firestore: FirebaseFirestore
) : WordsService {

    override val words: Flow<List<Words>>
        get() = firestore.collection(WORDS_COLLECTION).dataObjects()


    override suspend fun getWords(wordsId: String): Words? =
        firestore.collection(WORDS_COLLECTION).document(wordsId).get().await().toObject()

    override suspend fun findAWordsWithName(wordsName: String): Words? {
        val querySnapshot = firestore.collection(WORDS_COLLECTION)
            .whereEqualTo(WORDS_NAME_FIELD, wordsName)
            .get()
            .await()
        if (!querySnapshot.isEmpty) {
            // Lấy ra đối tượng đầu tiên từ kết quả truy vấn
            val document = querySnapshot.documents[0]
            // Chuyển đổi dữ liệu từ DocumentSnapshot thành đối tượng Words
            return document.toObject(Words::class.java)
        }
        return null
    }

    companion object {
        private const val WORDS_COLLECTION = "courses"
        private const val WORDS_NAME_FIELD = "name"
    }
}