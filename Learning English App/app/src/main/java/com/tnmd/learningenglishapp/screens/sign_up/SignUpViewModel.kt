

package com.tnmd.learningenglishapp.screens.sign_up

import android.net.Uri
import android.util.Log
import androidx.compose.runtime.mutableStateOf
import com.tnmd.learningenglishapp.LOGIN_SCREEN
import com.tnmd.learningenglishapp.common.ext.isValidEmail
import com.tnmd.learningenglishapp.common.ext.isValidPassword
import com.tnmd.learningenglishapp.common.ext.passwordMatches
import com.tnmd.learningenglishapp.common.snackbar.SnackbarManager
import com.tnmd.learningenglishapp.model.service.AuthenticationService
import com.tnmd.learningenglishapp.model.service.LogService
import com.tnmd.learningenglishapp.screens.LearningEnglishAppViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import javax.inject.Inject
import com.tnmd.learningenglishapp.R.string as AppText

@HiltViewModel
class SignUpViewModel @Inject constructor(
    private val authenticationService: AuthenticationService,
    logService: LogService
) : LearningEnglishAppViewModel(logService) {
    var uiState = mutableStateOf(SignUpUiState())
        private set
    private val _loginEvent = MutableSharedFlow<LogInEvent>()


    val email
        get() = uiState.value.email.trim()
    val password
        get() = uiState.value.password.trim()

    val username
        get() = uiState.value.username.trim()

    private val gender
        get() = uiState.value.gender

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

    private fun isGenderSelected(selectedGender: String): Boolean {
        return selectedGender.isNotBlank()
    }

    fun changeIsNextStep(isNextStep: Boolean) {
        uiState.value = uiState.value.copy(isNextStep = isNextStep)
    }

    private fun changeGender(gender: String) {
        uiState.value = uiState.value.copy(gender = gender)
    }

    fun onNextStep(
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
        } else {
            changeGender(selectedGender)
        }

        changeIsNextStep(true)

        Log.d("user1", email + password + username + selectedGender)

    }
    fun onLogin(openScreen: (String) -> Unit) = openScreen(LOGIN_SCREEN)
    fun onSignUp(
        image: Uri?
    ) {
        launchCatching {
            if(
            authenticationService.createAccount(
                email,
                password,
                username,
                gender,
                image
            )) {
                _loginEvent.emit(LogInEvent.Success)
            }
            Log.d("user", email + password + username + gender + image)
        }
    }

    sealed class LogInEvent {
        object Success : LogInEvent()
    }
}





