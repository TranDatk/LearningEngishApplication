

package com.tnmd.learningenglishapp.model.service

import android.net.Uri
import com.tnmd.learningenglishapp.model.Account
import kotlinx.coroutines.flow.Flow

interface AuthenticationService {
  val currentUserId: String
  val currentUserEmail: String
  val hasUser: Boolean

  val currentUser: Flow<Account>

  suspend fun authenticate(email: String, password: String) : Boolean
  suspend fun sendRecoveryEmail(email: String)
  suspend fun createAccount(email: String, password: String, username: String, selectedGender: String, image: Uri?) : Boolean
  suspend fun deleteAccount()
  suspend fun signOut()

  suspend fun getAccountAvatar(userId: String): String

  suspend fun updateEmail(newEmail: String): Boolean
}


