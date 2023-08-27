package com.tnmd.learningenglishapp.model

import com.google.firebase.firestore.DocumentId

data class Account_AccountLevel(
    @DocumentId val id : String = "",
    val dateJoin : String = "",
    val accountId : String = "",
    val accountLevelId : String = ""
)
