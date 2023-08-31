package com.tnmd.learningenglishapp.model.service

import com.google.android.gms.tasks.Task
import com.tnmd.learningenglishapp.model.Account
import com.tnmd.learningenglishapp.model.Courses
import kotlinx.coroutines.flow.Flow

interface CoursesService {
    val courses: Flow<List<Courses>>
    suspend fun getCourses(coursesId: String): Courses?
    suspend fun save(courses: Courses): String
    suspend fun update(courses: Courses)
    suspend fun delete(coursesId: String)
}
