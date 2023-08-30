

package com.tnmd.learningenglishapp.model.service

import com.tnmd.learningenglishapp.model.Account
import kotlinx.coroutines.flow.Flow

interface AccountService {
  val currentUserId: String
  val hasUser: Boolean

  val currentUser: Flow<Account>

  suspend fun authenticate(email: String, password: String) : Boolean
  suspend fun sendRecoveryEmail(email: String)
  suspend fun linkAccount(email: String, password: String)
  suspend fun deleteAccount()
  suspend fun signOut()
}


