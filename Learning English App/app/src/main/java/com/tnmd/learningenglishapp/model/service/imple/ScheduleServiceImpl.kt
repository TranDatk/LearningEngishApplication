package com.tnmd.learningenglishapp.model.service.imple

import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.dataObjects
import com.tnmd.learningenglishapp.model.Processes
import com.tnmd.learningenglishapp.model.Schedule
import com.tnmd.learningenglishapp.model.service.AccountService
import com.tnmd.learningenglishapp.model.service.AuthenticationService
import com.tnmd.learningenglishapp.model.service.LearnerService
import com.tnmd.learningenglishapp.model.service.ScheduleService
import com.tnmd.learningenglishapp.model.service.trace
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.tasks.await
import javax.inject.Inject


class ScheduleServiceImpl @Inject constructor(private val firestore: FirebaseFirestore, private val learnerService: LearnerService
) : ScheduleService {

    override val schedule: Flow<List<Schedule>>
        get() = learnerService.learner.flatMapLatest { learner ->
            firestore.collection(SCHEDULE_COLLECTION).whereEqualTo(
                LEARNER_ID_FIELD, learner.id)
                .dataObjects()
        }


    override suspend fun newSchedule(schedule: Schedule): String{
        val learnerId = learnerService.getLearner()?.id.orEmpty()
        val newSchedule = Schedule(id = " ",date = schedule.date, learnerId = learnerId)
        return firestore.collection(SCHEDULE_COLLECTION).add(newSchedule).await().id
    }

    override suspend fun removeSchedule(schedule: Schedule) {
        firestore.collection(SCHEDULE_COLLECTION).document(schedule.id).delete().await()
    }

    companion object {
        private const val SCHEDULE_COLLECTION = "schedule"
        private const val LEARNER_ID_FIELD = "learnerId"
        private const val DATE_FILED  = "date"
        private const val NEW_SCHEDULE_TRACE = "newSchedule"
    }
}