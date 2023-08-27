package com.tnmd.learningenglishapp.model

import com.google.firebase.firestore.DocumentId

data class Words_Courses(
    @DocumentId val id : String = "",
    val wordsId : String = "",
    val coursesId : String = ""

)
