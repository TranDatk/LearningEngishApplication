package com.tnmd.learningenglishapp.model

import com.google.firebase.firestore.DocumentId

data class Conversation(
    @DocumentId val id: String = "",
    val dateUpdate: String = "",
    val accountSendId : String = "",
    val accountReceive : String = ""
)
