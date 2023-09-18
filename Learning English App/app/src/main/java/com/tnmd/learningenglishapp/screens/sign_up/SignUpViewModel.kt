/*
Copyright 2022 Google LLC

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

    https://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
 */

package com.tnmd.learningenglishapp.screens.sign_up

import android.net.Uri
import android.util.Log
import androidx.compose.runtime.mutableStateOf
import com.tnmd.learningenglishapp.LOGIN_SCREEN
import com.tnmd.learningenglishapp.SETTINGS_SCREEN
import com.tnmd.learningenglishapp.SIGN_UP_SCREEN_TWO
import com.tnmd.learningenglishapp.common.ext.isValidEmail
import com.tnmd.learningenglishapp.common.ext.isValidPassword
import com.tnmd.learningenglishapp.common.ext.passwordMatches
import com.tnmd.learningenglishapp.common.snackbar.SnackbarManager
import com.tnmd.learningenglishapp.data.UserData
import com.tnmd.learningenglishapp.model.service.AuthenticationService
import com.tnmd.learningenglishapp.model.service.LogService
import com.tnmd.learningenglishapp.screens.LearningEnglishAppViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import io.getstream.chat.android.client.ChatClient
import javax.inject.Inject
import com.tnmd.learningenglishapp.R.string as AppText

@HiltViewModel
class SignUpViewModel @Inject constructor(
  private val authenticationService: AuthenticationService,
  logService: LogService,
  private val chatClient: ChatClient
) : LearningEnglishAppViewModel(logService) {

  private val userData = UserData()

  var uiState = mutableStateOf(SignUpUiState())
    private set

  private val email
    get() = uiState.value.email
  private val password
    get() = uiState.value.password

  private val username
    get() = uiState.value.username

  fun onEmailChange(newValue: String) {
    uiState.value = uiState.value.copy(email = newValue)
  }

  fun onPasswordChange(newValue: String) {
    uiState.value = uiState.value.copy(password = newValue)
  }

  fun onRepeatPasswordChange(newValue: String) {
    uiState.value = uiState.value.copy(repeatPassword = newValue)
  }

  fun onUsernameChange(newValue: String) {
    uiState.value = uiState.value.copy(username = newValue)
  }

  fun isGenderSelected(selectedGender: String): Boolean {
    return selectedGender.isNotBlank()
  }

  fun onNextStep(
    openScreen: (String) -> Unit,
    selectedGender: String
  ) {
    if (!email.isValidEmail()) {
      SnackbarManager.showMessage(AppText.email_error)
      return
    }

    if (!password.isValidPassword()) {
      SnackbarManager.showMessage(AppText.password_error)
      return
    }

    if (!password.passwordMatches(uiState.value.repeatPassword)) {
      SnackbarManager.showMessage(AppText.password_match_error)
      return
    }

    if (!isGenderSelected(selectedGender)) {
      SnackbarManager.showMessage(AppText.gender_choose)
      return
    }

    launchCatching {
      saveUserData(email,username,password,selectedGender)
      Log.d("user1", email + password + username)
      openScreen(SIGN_UP_SCREEN_TWO)
    }
  }

  fun onSignUp(openAndPopUp: (String, String) -> Unit, ) {

    launchCatching {
//      if (email != null && password != null && username != null && gender != null && image != null) {
//        authenticationService.createAccount(email, password, username, gender, image)
//      }
//      openAndPopUp(SETTINGS_SCREEN, LOGIN_SCREEN)

      Log.d("user", email + password + username)
      }
  }

  private fun saveUserData(
    email: String,
    username: String,
    password: String,
    gender: String
  ) {
    userData.email = email
    userData.username = username
    userData.password = password
    userData.gender = gender
  }

  fun saveProfileImage(imageUri: Uri?) {
    userData.imageUri = imageUri
  }

  private fun getUserData(): UserData {
    return userData
  }
}

