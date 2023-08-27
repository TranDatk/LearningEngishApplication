package com.tnmd.learningenglishapp.model

import com.google.firebase.firestore.DocumentId

data class Words(
    @DocumentId val id : String = "",
    val name : String = "",
    val means : String = "",
    val pronounce : String = "",
    val audioURL : String = ""
)
