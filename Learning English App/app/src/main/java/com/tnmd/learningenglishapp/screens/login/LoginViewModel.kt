
package com.tnmd.learningenglishapp.screens.login

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.tnmd.learningenglishapp.LOGIN_SCREEN
import com.tnmd.learningenglishapp.R
import com.tnmd.learningenglishapp.R.string as AppText
import com.tnmd.learningenglishapp.common.snackbar.SnackbarManager
import com.tnmd.learningenglishapp.SETTINGS_SCREEN
import com.tnmd.learningenglishapp.screens.LearningEnglishAppViewModel
import com.tnmd.learningenglishapp.common.ext.isValidEmail
import com.tnmd.learningenglishapp.model.service.AuthenticationService
import com.tnmd.learningenglishapp.model.service.LogService
import dagger.hilt.android.lifecycle.HiltViewModel
import io.getstream.chat.android.client.ChatClient
import io.getstream.chat.android.client.call.enqueue
import io.getstream.chat.android.client.models.User
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
  private val authenticationService: AuthenticationService,
  logService: LogService,
  private val client: ChatClient
) : LearningEnglishAppViewModel(logService) {
  var uiState = mutableStateOf(LoginUiState())
    private set

  private val _loadingState = MutableLiveData<UiLoadingState>()
  val loadingState : LiveData<UiLoadingState>
    get() = _loadingState

  private val email
    get() = uiState.value.email
  private val password
    get() = uiState.value.password

  private val token = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJ1c2VyX2lkIjoidGFuZGF0a3QwMDIifQ.eW1kkDTwiOGTmMeVkTrLgtG_xeAzCs66Y_jC2MDrLcg"


  val username: String
    get() {
      return if (email.contains("@")) {
        email.substring(0, email.indexOf("@"))
      } else {
        ""
      }
    }


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

      fun LoginRegisteredUser(username: String, token: String) {
      val user = User(id = username, name = username)
        _loadingState.value = UiLoadingState.Loading
      client.connectUser(
        user = user,
        token = token
      ).enqueue { result ->
        if (result.isSuccess) {
          Log.d("dat123456", "đăng ký người dùng thành công")
        } else {
          Log.d("dat12345", "đăng ký người dùng thất bại")
        }
      }
    }

    launchCatching {
      if(authenticationService.authenticate(email, password)){
        _loginEvent.emit(LogInEvent.Success)
          LoginRegisteredUser(username, token)
          SnackbarManager.showMessage(AppText.login_success)
      }else{
        openAndPopUp(SETTINGS_SCREEN, LOGIN_SCREEN)
        SnackbarManager.showMessage(AppText.login_fail)
      }
    }


  }

  fun onForgotPasswordClick() {
    if (!email.isValidEmail()) {
      SnackbarManager.showMessage(AppText.email_error)
      return
    }

    launchCatching {
      authenticationService.sendRecoveryEmail(email)
      SnackbarManager.showMessage(AppText.recovery_email_sent)
    }
  }

  fun LoginRegisteredUser(username: String, token: String) {
    val user = User(id = username, name = username)
    _loadingState.value = UiLoadingState.Loading
    client.connectUser(
      user = user,
      token = token
    ).enqueue { result ->
      if (result.isSuccess) {
        Log.d("dat123456", "đăng ký người dùng thành công")
      } else {
        Log.d("dat12345", "đăng ký người dùng thất bại")
      }
    }
  }

  fun loginGuestUser() {
    _loadingState.value = UiLoadingState.Loading
    client.connectGuestUser(
      userId = username,
      username = username
    ).enqueue { result ->
      _loadingState.value = UiLoadingState.NotLoading
      if (result.isSuccess) {
        launchCatching { _loginEvent.emit(LogInEvent.Success) }
        Log.d("dat123456", "đăng ký người dùng thành công")
      } else {
        Log.d("dat12345", "đăng ký người dùng thất bại")
      }

    }
  }

  sealed class LogInEvent {
    data class ErrorLogIn(val errorLogIn: String): LogInEvent()
    object Success : LogInEvent()
  }

  sealed class UiLoadingState {
    object Loading: UiLoadingState()
    object NotLoading: UiLoadingState()
  }
}


