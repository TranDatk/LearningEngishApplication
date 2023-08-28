package com.tnmd.learningenglishapp.model

import com.google.firebase.firestore.DocumentId


data class Account (
     @DocumentId val id : String = "",
     val email : String = "",
     val password : String = "",
     val avatar : String = "",
     val status : Boolean = false,
     var learnerId: String = ""
)
