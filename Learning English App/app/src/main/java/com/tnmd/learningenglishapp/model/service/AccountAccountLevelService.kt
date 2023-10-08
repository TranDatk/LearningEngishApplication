package com.tnmd.learningenglishapp.model.service

interface AccountAccountLevelService {

    suspend fun getAccountLevelId(accountId: String): String?
    suspend fun updateAccountLevelId(accountId: String, newAccountLevelId: String): Boolean
}