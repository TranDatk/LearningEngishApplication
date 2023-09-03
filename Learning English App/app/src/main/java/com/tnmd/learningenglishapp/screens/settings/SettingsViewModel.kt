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

package com.tnmd.learningenglishapp.screens.settings

import androidx.compose.runtime.mutableStateOf
import com.tnmd.learningenglishapp.LOGIN_SCREEN
import com.tnmd.learningenglishapp.SIGN_UP_SCREEN
import com.tnmd.learningenglishapp.SPLASH_SCREEN
import com.tnmd.learningenglishapp.screens.LearningEnglishAppViewModel
import com.tnmd.learningenglishapp.model.service.AuthenticationService
import com.tnmd.learningenglishapp.model.service.LogService
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
  logService: LogService,
  private val authenticationService: AuthenticationService
) : LearningEnglishAppViewModel(logService) {
  val uiState = mutableStateOf(SettingsUiState(isAnonymousAccount = authenticationService.hasUser))

  fun onLoginClick(openScreen: (String) -> Unit) = openScreen(LOGIN_SCREEN)

  fun onSignUpClick(openScreen: (String) -> Unit) = openScreen(SIGN_UP_SCREEN)

  fun onSignOutClick(restartApp: (String) -> Unit) {
    launchCatching {
      authenticationService.signOut()
      restartApp(SPLASH_SCREEN)
    }
  }

  fun onDeleteMyAccountClick(restartApp: (String) -> Unit) {
    launchCatching {
      authenticationService.deleteAccount()
      restartApp(SPLASH_SCREEN)
    }
  }
}
