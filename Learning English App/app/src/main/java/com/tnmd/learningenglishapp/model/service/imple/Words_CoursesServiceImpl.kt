package com.tnmd.learningenglishapp.model.service.imple

import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.dataObjects
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.firestore.ktx.toObjects
import com.tnmd.learningenglishapp.model.Courses
import com.tnmd.learningenglishapp.model.Words
import com.tnmd.learningenglishapp.model.Words_Courses
import com.tnmd.learningenglishapp.model.service.AuthenticationService
import com.tnmd.learningenglishapp.model.service.Words_CoursesService
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class Words_CoursesServiceImpl @Inject
constructor(private val firestore: FirebaseFirestore, private val auth: AuthenticationService
) : Words_CoursesService {
    override val words_courses: Flow<List<Words_Courses>>
        get() = firestore.collection(WORDS_COURSES_COLLECTION).dataObjects()


    override suspend fun getWordsFromCourses(coursesId: String): List<Words?> {
        val wordsCoursesQuery = firestore.collection(WORDS_COURSES_COLLECTION)
            .whereEqualTo(COURSES_ID_FIELD, coursesId)
            .get()
            .await()

        val listWordsCourses = wordsCoursesQuery.toObjects<Words_Courses>()

        val listWords = mutableListOf<Words?>()

        for (wordsCourses in listWordsCourses) {
            val wordsDocument = firestore.collection(WORDS_COLLECTION)
                .document(wordsCourses.wordsId)
                .get()
                .await()

            val words = wordsDocument.toObject<Words>()
            listWords.add(words)
        }
        return listWords
    }


    companion object {
        private const val WORDS_ID_FIELD = "wordsId"
        private const val WORDS_COURSES_COLLECTION = "words_courses"
        private const val COURSES_ID_FIELD = "coursesId"
        private const val WORDS_COLLECTION = "words"
    }
}