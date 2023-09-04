package com.tnmd.learningenglishapp.model.service

import com.tnmd.learningenglishapp.model.Account
import com.tnmd.learningenglishapp.model.Learner
import com.tnmd.learningenglishapp.model.Words
import kotlinx.coroutines.flow.Flow

interface AccountService {
    val account: Flow<Account>
    suspend fun getCurrentAccount(): Account?
    suspend fun getAccount(accountId: String): Account?
}