package com.tnmd.learningenglishapp.model

import com.google.firebase.firestore.DocumentId

data class Processes(
    @DocumentId val id: String = "",
    val processesLearn : Double = 0.0,
    val processesCheck : Double = 0.0,
    val learnerId : String = "",
    val coursesId : String = ""

)
