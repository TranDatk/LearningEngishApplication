package com.tnmd.learningenglishapp.model.service.imple

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.tnmd.learningenglishapp.model.AccountLevel
import com.tnmd.learningenglishapp.model.service.AccountLevelService
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class AccountLevelServiceImpl@Inject constructor(private val auth: FirebaseAuth, private val firestore: FirebaseFirestore) :
    AccountLevelService {

    override suspend fun getAllAccountLevels(): List<AccountLevel>? {
        return try {
            val accountLevelCollection = firestore.collection(ACCOUNT_LEVEL_COLLECTION)
                .get()
                .await()

            val accountLevels = mutableListOf<AccountLevel>()
            for (document in accountLevelCollection) {
                val level = document.getString("level") ?: continue
                val descriptionLevel = document.getString("descriptionLevel") ?: ""
                val validityPeriod = document.getLong("validityPeriod")?.toInt() ?: 0

                val accountLevel = AccountLevel(document.id, level, descriptionLevel, validityPeriod)
                accountLevels.add(accountLevel)
            }

            accountLevels
        } catch (e: Exception) {
            null // Xử lý lỗi khi truy vấn dữ liệu
        }
    }

    override suspend fun getLevelAccount(accountLevelId: String): String? {
        return try {
            val accountLevelDocument = firestore.collection(ACCOUNT_LEVEL_COLLECTION)
                .document(accountLevelId)
                .get()
                .await()

            if (accountLevelDocument.exists()) {
                val level = accountLevelDocument.getString("level")
                level
            } else {
                null // Trả về null nếu không tìm thấy tài khoản
            }
        } catch (e: Exception) {
            null // Xử lý lỗi khi truy vấn dữ liệu
        }
    }

    override  suspend fun getDescriptionLevel(accountLevelId: String): String? {
        return try {
            val accountLevelDocument = firestore.collection(ACCOUNT_LEVEL_COLLECTION)
                .document(accountLevelId)
                .get()
                .await()

            if (accountLevelDocument.exists()) {
                val descriptionLevel = accountLevelDocument.getString("descriptionLevel")
                descriptionLevel
            } else {
                null // Trả về null nếu không tìm thấy tài khoản
            }
        } catch (e: Exception) {
            null // Xử lý lỗi khi truy vấn dữ liệu
        }
    }
   override suspend fun getValidityPeriod(accountLevelId: String): Int? {
        return try {
            val accountLevelDocument = firestore.collection(ACCOUNT_LEVEL_COLLECTION)
                .document(accountLevelId)
                .get()
                .await()

            if (accountLevelDocument.exists()) {
                val validityPeriod = accountLevelDocument.getLong("validityPeriod")
                validityPeriod?.toInt() // Chuyển đổi từ Long sang Int
            } else {
                null // Trả về null nếu không tìm thấy tài khoản
            }
        } catch (e: Exception) {
            null // Xử lý lỗi khi truy vấn dữ liệu
        }
    }

    companion object {
        private const val ACCOUNT_LEVEL_COLLECTION = "accountLevel"

    }
}