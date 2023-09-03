package com.tnmd.learningenglishapp.model.service.imple

import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.dataObjects
import com.tnmd.learningenglishapp.model.Account
import com.tnmd.learningenglishapp.model.Learner
import com.tnmd.learningenglishapp.model.Words
import com.google.firebase.firestore.ktx.toObject
import com.tnmd.learningenglishapp.model.service.AccountService
import com.tnmd.learningenglishapp.model.service.AuthenticationService
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class AccountServiceImpl @Inject
constructor(private val firestore: FirebaseFirestore, private val auth: AuthenticationService
) : AccountService {
    override val account: Flow<Account?>
        get() = auth.currentUser.flatMapLatest { user ->
            firestore.collection(ACCOUNT_COLLECTION).document(user.id).dataObjects()
        }

    override suspend fun getCurrentAccount(): Account? =
       firestore.collection(ACCOUNT_COLLECTION).document(auth.currentUserId).get().await().toObject<Account>()

    override suspend fun getAccount(accountId: String): Account? =
        firestore.collection(ACCOUNT_COLLECTION).document(accountId).get().await().toObject<Account>()

    companion object {
        private const val ACCOUNT_COLLECTION = "account"
    }
}