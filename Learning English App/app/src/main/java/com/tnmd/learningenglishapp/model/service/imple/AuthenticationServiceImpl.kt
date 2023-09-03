

package com.tnmd.learningenglishapp.model.service.imple

import android.util.Log
import androidx.appcompat.widget.AppCompatTextView
import com.tnmd.learningenglishapp.model.Account
import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthException
import com.google.firebase.firestore.FirebaseFirestore
import com.tnmd.learningenglishapp.R
import com.tnmd.learningenglishapp.common.snackbar.SnackbarManager
import com.tnmd.learningenglishapp.model.Learner
import com.tnmd.learningenglishapp.model.service.AuthenticationService
import com.tnmd.learningenglishapp.model.service.trace
import javax.inject.Inject
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlinx.coroutines.tasks.await

class AuthenticationServiceImpl @Inject constructor(private val auth: FirebaseAuth, private val firestore: FirebaseFirestore) : AuthenticationService {

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

  override suspend fun authenticate(email: String, password: String): Boolean {
    return suspendCancellableCoroutine { continuation ->
      auth.signInWithEmailAndPassword(email, password)
        .addOnCompleteListener { task ->
          if (task.isSuccessful) {
            continuation.resume(true) {
            }
          } else {
            continuation.resume(false) {

            }
          }
        }
        .addOnFailureListener {
          continuation.resume(false) {
          }
        }
    }
  }

  override suspend fun sendRecoveryEmail(email: String) {
    auth.sendPasswordResetEmail(email).await()
  }

  override suspend fun createAccount(email: String, password: String, username: String) {
    try {
      val authResult = auth.createUserWithEmailAndPassword(email, password).await()
      if (authResult.user != null) {
        val userId = authResult.user!!.uid
        val learnerUsername = hashMapOf(
          "username" to username
        )
        val collectionReference = firestore.collection(LEARNER_COLLECTION)
        collectionReference.add(learnerUsername)
          .addOnSuccessListener { documentReference ->
            val learnerID = documentReference.id
            // Cập nhật thông tin Account
            val account = Account(userId, "", true, learnerID)
            firestore.collection(ACCOUNT_COLLECTION).document(userId)
              .set(account)
              .addOnSuccessListener {
                SnackbarManager.showMessage(R.string.create_success)
              }
              .addOnFailureListener { e ->
                Log.d("dat", "Lỗi khi đặt thông tin Account: ${e.message}")
              }
          }
          .addOnFailureListener { e ->
            Log.d("dat", "Lỗi khi thêm tài liệu Learner: ${e.message}")
          }
      }
    } catch (e: FirebaseAuthException) {
      Log.d("dat", "Đăng ký thất bại: ${e.message}")
    }
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
    private const val COMPLETE_AUTH = "Đăng nhập thành công"
    private const val FAILURE_AUTH_EMAIL = "Email không tồn tại"
    private const val FAILURE_AUTH_PASS = "Đăng nhập thành công"
    private const val ACCOUNT_COLLECTION = "account"
    private const val LEARNER_COLLECTION = "learner"
  }
}
