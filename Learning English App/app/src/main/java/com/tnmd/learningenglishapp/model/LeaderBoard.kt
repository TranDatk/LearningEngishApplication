package com.tnmd.learningenglishapp.model

import com.google.firebase.firestore.DocumentId

data class LeaderBoard(
    @DocumentId val id: String = "",
    val username: String = "",
    val scoreTotal : Int = 0,
    val ranking : Int = 0,
    val accountId : String = "",
    val ScoreId : String = ""
)
