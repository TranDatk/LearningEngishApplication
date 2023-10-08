package com.tnmd.learningenglishapp.model.service

import com.tnmd.learningenglishapp.model.AccountLevel

interface AccountLevelService {

    suspend fun getLevelAccount(accountLevelId: String): String?

    suspend fun getDescriptionLevel(accountLevelId: String): String?
    suspend fun getValidityPeriod(accountLevelId: String): Int?
    suspend fun getAllAccountLevels(): List<AccountLevel>?
}