

package com.tnmd.learningenglishapp.model.service

import com.tnmd.learningenglishapp.model.Account
import kotlinx.coroutines.flow.Flow

interface AuthenticationService {
  val currentUserId: String
  val hasUser: Boolean

  val currentUser: Flow<Account>

  suspend fun authenticate(email: String, password: String) : Boolean
  suspend fun sendRecoveryEmail(email: String)
  suspend fun createAccount(email: String, password: String, username: String)
  suspend fun deleteAccount()
  suspend fun signOut()
}


