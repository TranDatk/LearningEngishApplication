
package com.tnmd.learningenglishapp.screens.login

import android.content.Intent
import androidx.compose.runtime.mutableStateOf
import androidx.core.content.ContextCompat.startActivity
import com.tnmd.learningenglishapp.LOGIN_SCREEN
import com.tnmd.learningenglishapp.R.string as AppText
import com.tnmd.learningenglishapp.common.snackbar.SnackbarManager
import com.tnmd.learningenglishapp.SETTINGS_SCREEN
import com.tnmd.learningenglishapp.activity.MainActivity
import com.tnmd.learningenglishapp.screens.LearningEnglishAppViewModel
import com.tnmd.learningenglishapp.common.ext.isValidEmail
import com.tnmd.learningenglishapp.model.service.AccountService
import com.tnmd.learningenglishapp.model.service.LogService
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
  private val accountService: AccountService,
  logService: LogService
) : LearningEnglishAppViewModel(logService) {
  var uiState = mutableStateOf(LoginUiState())
    private set

  private val email
    get() = uiState.value.email
  private val password
    get() = uiState.value.password

  private val _loginEvent = MutableSharedFlow<LogInEvent>()
  val loginEvent = _loginEvent.asSharedFlow()

  fun onEmailChange(newValue: String) {
    uiState.value = uiState.value.copy(email = newValue)
  }

  fun onPasswordChange(newValue: String) {
    uiState.value = uiState.value.copy(password = newValue)
  }

  fun onSignInClick(openAndPopUp: (String, String) -> Unit) {
    if (!email.isValidEmail()) {
      SnackbarManager.showMessage(AppText.email_error)
      return
    }

    if (password.isBlank()) {
      SnackbarManager.showMessage(AppText.empty_password_error)
      return
    }

    launchCatching {
      if( accountService.authenticate(email, password) == true){
        _loginEvent.emit(LogInEvent.Success)
      }else{
        openAndPopUp(SETTINGS_SCREEN, LOGIN_SCREEN)
      }
    }
  }

  fun onForgotPasswordClick() {
    if (!email.isValidEmail()) {
      SnackbarManager.showMessage(AppText.email_error)
      return
    }

    launchCatching {
      accountService.sendRecoveryEmail(email)
      SnackbarManager.showMessage(AppText.recovery_email_sent)
    }
  }

  sealed class LogInEvent {
    data class ErrorLogIn(val errorLogIn: String): LogInEvent()
    object Success : LogInEvent()
  }
}


