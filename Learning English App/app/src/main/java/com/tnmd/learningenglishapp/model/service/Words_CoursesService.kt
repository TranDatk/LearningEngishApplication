package com.tnmd.learningenglishapp.model.service

import com.tnmd.learningenglishapp.model.Words
import com.tnmd.learningenglishapp.model.Words_Courses
import kotlinx.coroutines.flow.Flow

interface Words_CoursesService {
    val words_courses: Flow<List<Words_Courses>>

    suspend fun getWordsFromCourses(coursesId: String): List<Words?>
}