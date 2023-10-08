package com.tnmd.learningenglishapp.model.service.imple

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.tnmd.learningenglishapp.model.service.AccountAccountLevelService

import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class AccountAccountLevelServiceImpl@Inject constructor(private val auth: FirebaseAuth, private val firestore: FirebaseFirestore) :
    AccountAccountLevelService  {

    override suspend fun getAccountLevelId(accountId: String): String? {
        return try {
            val querySnapshot = firestore.collection(ACCOUNT_ACCOUNT_LEVEL_COLLECTION)
                .whereEqualTo("accountId", accountId)
                .limit(1)
                .get()
                .await()

            if (!querySnapshot.isEmpty) {
                val document = querySnapshot.documents[0]
                val accountLevelId = document.getString("accountLevelId")
                accountLevelId
            } else {
                null // Trả về null nếu không tìm thấy accountLevelId cho accountId cung cấp
            }
        } catch (e: Exception) {
            null // Xử lý lỗi khi truy vấn dữ liệu
        }
    }

   override suspend fun updateAccountLevelId(accountId: String, newAccountLevelId: String): Boolean {
        return try {
            val querySnapshot = firestore.collection(ACCOUNT_ACCOUNT_LEVEL_COLLECTION)
                .whereEqualTo("accountId", accountId)
                .limit(1)
                .get()
                .await()

            if (!querySnapshot.isEmpty) {
                val document = querySnapshot.documents[0]
                val documentId = document.id

                // Tạo một map chứa dữ liệu mới cần cập nhật
                val data = hashMapOf(
                    "accountLevelId" to newAccountLevelId
                )

                // Thực hiện cập nhật dữ liệu trong Firestore
                firestore.collection(ACCOUNT_ACCOUNT_LEVEL_COLLECTION)
                    .document(documentId)
                    .update(data as Map<String, Any>)
                    .await()

                true // Trả về true nếu cập nhật thành công
            } else {
                false // Trả về false nếu không tìm thấy accountId để cập nhật
            }
        } catch (e: Exception) {
            false // Xử lý lỗi khi cập nhật dữ liệu
        }
    }

    companion object {
            private const val ACCOUNT_ACCOUNT_LEVEL_COLLECTION = "account_accountLevel"
        }
}