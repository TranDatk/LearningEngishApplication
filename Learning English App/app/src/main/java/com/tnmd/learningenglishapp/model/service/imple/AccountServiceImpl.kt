

package com.tnmd.learningenglishapp.model.service.imple

import com.tnmd.learningenglishapp.model.Account
import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.tnmd.learningenglishapp.model.service.AccountService
import com.tnmd.learningenglishapp.model.service.trace
import javax.inject.Inject
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await

class AccountServiceImpl @Inject constructor(private val auth: FirebaseAuth) : AccountService {

  override val currentUserId: String
    get() = auth.currentUser?.uid.orEmpty()

  override val hasUser: Boolean
    get() = auth.currentUser != null

  override val currentUser: Flow<Account>
    get() = callbackFlow {
      val listener =
        FirebaseAuth.AuthStateListener { auth ->
          this.trySend(auth.currentUser?.let { Account(it.uid) } ?: Account())
        }
      auth.addAuthStateListener(listener)
      awaitClose { auth.removeAuthStateListener(listener) }
    }

  override suspend fun authenticate(email: String, password: String) {
    auth.signInWithEmailAndPassword(email, password).await()
  }

  override suspend fun sendRecoveryEmail(email: String) {
    auth.sendPasswordResetEmail(email).await()
  }

  override suspend fun linkAccount(email: String, password: String): Unit =
    trace(LINK_ACCOUNT_TRACE) {
      val credential = EmailAuthProvider.getCredential(email, password)
      auth.currentUser!!.linkWithCredential(credential).await()
    }

  override suspend fun deleteAccount() {
    auth.currentUser!!.delete().await()
  }

  override suspend fun signOut() {
    if (auth.currentUser!!.isAnonymous) {
      auth.currentUser!!.delete()
    }
    auth.signOut()

  }

  companion object {
    private const val LINK_ACCOUNT_TRACE = "linkAccount"
  }
}
