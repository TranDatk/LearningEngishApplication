package com.tnmd.learningenglishapp.model

import com.google.firebase.firestore.DocumentId
import java.util.Date

data class Schedule(
    @DocumentId val id : String = "",
    val date : String = "",
    val learnerId : String = ""
)
