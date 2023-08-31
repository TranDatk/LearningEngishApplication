package com.tnmd.learningenglishapp.model.service.imple

import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.dataObjects
import com.tnmd.learningenglishapp.model.Courses
import com.tnmd.learningenglishapp.model.service.AccountService
import com.tnmd.learningenglishapp.model.service.CoursesService
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flatMapLatest
import javax.inject.Inject

class CoursesServiceImpl @Inject
constructor(private val firestore: FirebaseFirestore, private val auth: AccountService
) : CoursesService {

    @OptIn(ExperimentalCoroutinesApi::class)
    override val courses: Flow<List<Courses>>
        get() =
            auth.currentUser.flatMapLatest { user ->
                firestore.collection(TASK_COLLECTION).whereEqualTo(USER_ID_FIELD, user.id).dataObjects()
            }


    override suspend fun getCourses(coursesId: String): Courses? {
        TODO("Not yet implemented")
    }

    override suspend fun save(courses: Courses): String {
        TODO("Not yet implemented")
    }

    override suspend fun update(courses: Courses) {
        TODO("Not yet implemented")
    }

    override suspend fun delete(coursesId: String) {
        TODO("Not yet implemented")
    }

    companion object {
        private const val USER_ID_FIELD = "userId"
        private const val TASK_COLLECTION = "tasks"
        private const val SAVE_TASK_TRACE = "saveTask"
        private const val UPDATE_TASK_TRACE = "updateTask"
    }
}