

package com.tnmd.learningenglishapp.model.service.imple

import android.content.Context
import android.net.Uri
import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthException
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.tnmd.learningenglishapp.R
import com.tnmd.learningenglishapp.common.snackbar.SnackbarManager
import com.tnmd.learningenglishapp.model.Account
import com.tnmd.learningenglishapp.model.service.AuthenticationService
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlinx.coroutines.tasks.await
import java.io.InputStream
import javax.inject.Inject

class AuthenticationServiceImpl @Inject constructor(private val auth: FirebaseAuth, private val firestore: FirebaseFirestore) : AuthenticationService {

  @ApplicationContext
  @Inject
  lateinit var context: Context
  override val currentUserId: String
    get() = auth.currentUser?.uid.orEmpty()

  override val currentUserEmail: String
    get() = auth.currentUser?.email.orEmpty()

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
          Log.d("dat", "Đăng nhập thất bại")
        }
    }
  }

  override suspend fun sendRecoveryEmail(email: String) {
    auth.sendPasswordResetEmail(email).await()
  }

  override suspend fun createAccount(email: String, password: String, username: String, selectedGender: String, imageUri: Uri?): Boolean {
    try {
      val authResult = auth.createUserWithEmailAndPassword(email, password).await()
      if (authResult.user != null) {
        val userId = authResult.user!!.uid

        // Xác định URL ảnh dựa trên giới tính hoặc sử dụng URL mặc định
        val imageUrl = if (imageUri != null) {
          // Tải lên hình ảnh lên Firebase Storage và lấy URL sau khi tải lên
          val imageUrl = uploadImageToFirebaseStorage(userId, imageUri)
          imageUrl
        } else {
          if (selectedGender.equals("Nam")) {
            "https://firebasestorage.googleapis.com/v0/b/learningenglishapp-1e3bd.appspot.com/o/man.png?alt=media&token=c8de7ac0-0fd9-4ace-9d49-43e95221196a"
          } else {
            "https://firebasestorage.googleapis.com/v0/b/learningenglishapp-1e3bd.appspot.com/o/woman.png?alt=media&token=66c5fdee-b5f3-4a3e-aa15-15702c615d67"
          }
        }

        val learnerUsername = hashMapOf(
          "username" to username
        )

        val collectionReference = firestore.collection(LEARNER_COLLECTION)
        val documentReference = collectionReference.add(learnerUsername).await()
        val learnerID = documentReference.id

        val account = Account(userId, imageUrl, true, learnerID)
        val accountCollection = firestore.collection(ACCOUNT_COLLECTION)
        accountCollection.document(userId)
          .set(account)
          .addOnSuccessListener {
            SnackbarManager.showMessage(R.string.create_success)
          }
          .addOnFailureListener { e ->
            Log.d("dat", "Lỗi khi đặt thông tin Account: ${e.message}")
          }
        return true
      }
    } catch (e: FirebaseAuthException) {
      Log.d("dat", "Đăng ký thất bại: ${e.message}")
    }
    return false
  }

  private suspend fun uploadImageToFirebaseStorage(userId: String, imageUri: Uri): String {
    val storageReference = FirebaseStorage.getInstance().reference.child("images/$userId.jpg")
    val inputStream: InputStream? = context.contentResolver.openInputStream(imageUri)

    return try {
      val uploadTask = storageReference.putStream(inputStream!!)
      uploadTask.await()

      val downloadUrl = storageReference.downloadUrl.await()
      downloadUrl.toString()
    } catch (e: Exception) {
      Log.e("UploadImage", "Lỗi khi tải lên hình ảnh: ${e.message}")
      ""
    }
  }

  override suspend fun getAccountAvatar(userId: String): String {
    return try {
      val document = firestore.collection(ACCOUNT_COLLECTION).document(userId).get().await()
      if (document.exists()) {
        val account = document.toObject(Account::class.java)
        account?.avatar ?: "" // Trả về URL ảnh hoặc chuỗi trống nếu không tìm thấy
      } else {
        "" // Trả về chuỗi trống nếu không tìm thấy tài khoản với userId tương ứng
      }
    } catch (e: Exception) {
      Log.e("GetAccountAvatar", "Lỗi khi lấy URL ảnh: ${e.message}")
      ""
    }
  }

  override suspend fun updateEmail(newEmail: String): Boolean {
    return try {
      auth.currentUser?.updateEmail(newEmail)?.await()
      true // Trả về true nếu cập nhật thành công
    } catch (e: Exception) {
      Log.e("UpdateEmail", "Lỗi khi cập nhật email: ${e.message}")
      false // Trả về false nếu có lỗi xảy ra
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

    private const val ACCOUNT_COLLECTION = "account"
    private const val LEARNER_COLLECTION = "learner"
  }
}
