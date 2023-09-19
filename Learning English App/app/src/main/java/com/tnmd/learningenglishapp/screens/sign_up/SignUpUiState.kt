
package com.tnmd.learningenglishapp.screens.sign_up

data class SignUpUiState(
  val email: String = "",
  val password: String = "",
  val repeatPassword: String = "",
  val username : String = "",
  val Image: String ="",
  val isNextStep : Boolean = false,
  val gender : String = "Nam",
  val isValid : Boolean = false
)
