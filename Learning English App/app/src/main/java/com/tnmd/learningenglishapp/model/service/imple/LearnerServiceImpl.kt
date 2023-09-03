package com.tnmd.learningenglishapp.model.service.imple

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.dataObjects
import com.tnmd.learningenglishapp.model.Learner
import com.tnmd.learningenglishapp.model.service.AccountService
import com.tnmd.learningenglishapp.model.service.AuthenticationService
import com.tnmd.learningenglishapp.model.service.LearnerService
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.toList
import javax.inject.Inject

class LearnerServiceImpl @Inject
constructor(private val firestore: FirebaseFirestore, private val auth: FirebaseAuth
) : LearnerService {

    override val learner: Flow<Learner?>
        get() = firestore.collection(LEANER_COLLECTION).document(auth.currentUser?.uid.orEmpty()).dataObjects()


    companion object {
        private const val LEANER_COLLECTION = "learner"
    }
}