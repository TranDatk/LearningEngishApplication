package com.tnmd.learningenglishapp.model

import com.google.firebase.firestore.DocumentId

data class Courses(
    @DocumentId val id : String = "",
    val nameCourses : String = "",
    val description : String = "",
    val topicId : String = ""
)
