package com.tnmd.learningenglishapp.model

import com.google.firebase.firestore.DocumentId

data class Chat(
    @DocumentId val id: String = "",
    val message : String = "",
    val dateSend : String = "",
    val accountId : String = "",
    val conversationId : String = ""
)
