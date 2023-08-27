package com.tnmd.learningenglishapp.model

import com.google.firebase.firestore.DocumentId

data class Topic(
    @DocumentId val id : String = "",
    val name : String = ""
)
