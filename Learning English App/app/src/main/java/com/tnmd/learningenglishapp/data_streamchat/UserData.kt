package com.tnmd.learningenglishapp.data_streamchat

import android.net.Uri

data class UserData(
    var email: String = "",
    var username: String = "",
    var password: String = "",
    var gender: String = "",
    var imageUri: Uri? = null
)