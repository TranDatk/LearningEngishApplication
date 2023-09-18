package com.tnmd.learningenglishapp.screens.login

class TokenProvider(private val token: String) {
    fun loadToken(): String = token
}