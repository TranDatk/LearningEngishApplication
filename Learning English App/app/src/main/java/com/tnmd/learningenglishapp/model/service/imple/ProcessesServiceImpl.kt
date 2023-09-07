package com.tnmd.learningenglishapp.model.service.imple

import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.dataObjects
import com.google.firebase.storage.FirebaseStorage
import com.tnmd.learningenglishapp.model.Learner
import com.tnmd.learningenglishapp.model.Processes
import com.tnmd.learningenglishapp.model.service.AccountService
import com.tnmd.learningenglishapp.model.service.AuthenticationService
import com.tnmd.learningenglishapp.model.service.LearnerService
import com.tnmd.learningenglishapp.model.service.ProcessesService
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.runBlocking
import javax.inject.Inject

class ProcessesServiceImpl @Inject
constructor(private val firestore: FirebaseFirestore, private val learnerService: LearnerService
) : ProcessesService {

    override val processes: Flow<List<Processes>>
        get() = learnerService.learner.flatMapLatest { learner ->
            firestore.collection(PROCESSES_COLLECTION).whereEqualTo(LEARNER_ID_FIELD, learner.id)
                .dataObjects()
        }

    companion object {
        private const val PROCESSES_COLLECTION = "processes"
        private const val LEARNER_ID_FIELD = "learnerId"
    }
}