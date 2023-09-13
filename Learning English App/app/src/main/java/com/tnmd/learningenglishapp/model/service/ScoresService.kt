package com.tnmd.learningenglishapp.model.service
import com.tnmd.learningenglishapp.model.Scores
import kotlinx.coroutines.flow.Flow

interface ScoresService {
    val scores: Flow<List<Scores>>
    suspend fun getScoreByCoursesId(coursesId: String): Scores?
    suspend fun updateScore(score : Scores)
}