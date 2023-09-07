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
