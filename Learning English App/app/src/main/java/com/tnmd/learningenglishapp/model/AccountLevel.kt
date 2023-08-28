package com.tnmd.learningenglishapp.model

import com.google.firebase.firestore.DocumentId

data class AccountLevel(
    @DocumentId val id : String = "",
    val level : String = "",
    val descriptionLevel : String = "",
    val validityPeriod : Int = 0
)
