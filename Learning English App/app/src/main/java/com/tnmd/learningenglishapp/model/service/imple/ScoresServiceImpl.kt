package com.tnmd.learningenglishapp.model.service.imple

import android.util.Log
import com.google.firebase.firestore.Filter
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.dataObjects
import com.tnmd.learningenglishapp.model.Scores
import com.tnmd.learningenglishapp.model.service.AccountService
import com.tnmd.learningenglishapp.model.service.LearnerService
import com.tnmd.learningenglishapp.model.service.ScoresService
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class ScoresServiceImpl
@Inject
constructor(private val firestore: FirebaseFirestore, private val learnerService : LearnerService) :
ScoresService {
    override val scores: Flow<List<Scores>>
        get() =
            learnerService.learner.flatMapLatest { learner ->
                firestore.collection(SCORES_COLLECTION).whereEqualTo(LEARNER_ID_FILED, learner.id).dataObjects()
            }

    override suspend fun getScoreByCoursesId(coursesId: String): Scores? {
        val scores = learnerService.learner.flatMapLatest { learner ->
            firestore.collection(SCORES_COLLECTION)
                .where(Filter.or(
                    Filter.equalTo(LEARNER_ID_FILED,learner.id),
                    Filter.equalTo(COURSES_ID_FILED,coursesId))
                )
                .dataObjects<Scores>()
        }.firstOrNull() // Sử dụng firstOrNull thay vì first để tránh ngoại lệ nếu danh sách rỗng
        Log.d("trandat",scores?.getOrNull(0).toString())
        return scores?.getOrNull(0) // Sử dụng getOrNull để tránh lỗi nếu danh sách scores rỗng hoặc không có phần tử nào.
    }

    override suspend fun updateScore(score: Scores) {
        firestore.collection(SCORES_COLLECTION).document(score.id).set(score).await()
    }

    companion object {
        private const val SCORES_COLLECTION = "scores"
        private const val LEARNER_ID_FILED = "learnerId"
        private const val COURSES_ID_FILED  = "coursesId"
    }
}