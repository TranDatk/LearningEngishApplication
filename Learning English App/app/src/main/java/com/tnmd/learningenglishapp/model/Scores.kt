package com.tnmd.learningenglishapp.model

import com.google.firebase.firestore.DocumentId

data class Scores(
    @DocumentId val id: String = "",
    val score : Int = 0,
    val learnerId : String = "",
    val coursesId : String = ""
)
