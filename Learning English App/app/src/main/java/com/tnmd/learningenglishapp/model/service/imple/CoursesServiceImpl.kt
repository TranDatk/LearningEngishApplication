package com.tnmd.learningenglishapp.model.service.imple

import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.dataObjects
import com.google.firebase.firestore.ktx.toObject
import com.tnmd.learningenglishapp.model.Courses
import com.tnmd.learningenglishapp.model.service.AuthenticationService
import com.tnmd.learningenglishapp.model.service.CoursesService
import com.tnmd.learningenglishapp.model.service.trace
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class CoursesServiceImpl @Inject
constructor(private val firestore: FirebaseFirestore, private val auth: AuthenticationService
) : CoursesService {

    override val courses: Flow<List<Courses>>
        get() = firestore.collection(COURSES_COLLECTION).dataObjects()


    override suspend fun getCourses(coursesId: String): Courses? =
        firestore.collection(COURSES_COLLECTION).document(coursesId).get().await().toObject()

    override suspend fun save(courses: Courses): String =
        trace(SAVE_COURSES_TRACE){
            firestore.collection(COURSES_COLLECTION).add(courses).await().id
        }

    override suspend fun update(courses: Courses): Unit =
        trace(UPDATE_COURES_TRACE){
            firestore.collection(COURSES_COLLECTION).document(courses.id).set(courses).await()
        }

    override suspend fun delete(coursesId: String) {
        firestore.collection(COURSES_COLLECTION).document(coursesId).delete().await()
    }

    companion object {
        private const val USER_ID_FIELD = "userId"
        private const val COURSES_COLLECTION = "courses"
        private const val SAVE_COURSES_TRACE = "saveCourses"
        private const val UPDATE_COURES_TRACE = "updateCourses"
    }
}