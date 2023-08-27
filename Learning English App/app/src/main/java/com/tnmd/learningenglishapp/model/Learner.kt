package com.tnmd.learningenglishapp.model

import com.google.firebase.firestore.DocumentId

data class Learner (
    @DocumentId val id: String = "",
    val username: String = ""
)