package com.tnmd.learningenglishapp.screens.chat

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.viewModelScope
import com.tnmd.learningenglishapp.LOGIN_SCREEN
import com.tnmd.learningenglishapp.LearningEnglishAppState
import com.tnmd.learningenglishapp.R
import com.tnmd.learningenglishapp.SETTINGS_SCREEN
import com.tnmd.learningenglishapp.USERPROFILE_SCREEN
import com.tnmd.learningenglishapp.common.snackbar.SnackbarManager
import com.tnmd.learningenglishapp.data_streamchat.StreamTokenApi
import com.tnmd.learningenglishapp.model.service.AccountService
import com.tnmd.learningenglishapp.model.service.AuthenticationService
import com.tnmd.learningenglishapp.model.service.LearnerService
import com.tnmd.learningenglishapp.model.service.LogService
import com.tnmd.learningenglishapp.screens.LearningEnglishAppViewModel
import com.tnmd.learningenglishapp.screens.login.StreamTokenProvider
import com.tnmd.learningenglishapp.screens.sign_up.SignUpUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import io.getstream.chat.android.client.ChatClient
import io.getstream.chat.android.client.models.User
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch
import java.util.UUID
import javax.inject.Inject

@HiltViewModel
class ChannelListViewModal @Inject constructor(
    private val client: ChatClient,
    private val authenticationService: AuthenticationService,
    logService: LogService,
    private val learnerService: LearnerService
) : LearningEnglishAppViewModel(logService){
    var uiState = mutableStateOf(ScreenUiState())
        private set
    private val _createChannelEvent = MutableSharedFlow<CreateChannelEvent>()
    val createChannelEvent = _createChannelEvent.asSharedFlow()
    private val userid = authenticationService.currentUserId
    private val currentUser = authenticationService.currentUser
    init {
        viewModelScope.launch {
            initializeUser()
        }
    }

    private suspend fun initializeUser() {
        try {
            val username = learnerService.getLearnerUsername()
            val avatar = authenticationService.getAccountAvatar(userid)
            val email = authenticationService.currentUserEmail
            onEmailChange(email)
            onUsernameChange(username)
            onAvatarChange(avatar)
            getTokenAndConnectUser(userid)
            Log.d("token", userid)
        } catch (e: Exception) {
            handleInitializationError(e)
        }
    }

    private fun handleInitializationError(exception: Exception) {
        Log.e("Error", "Initialization error", exception)
    }


    private val _drawerShouldBeOpened = MutableStateFlow(false)
    val drawerShouldBeOpened = _drawerShouldBeOpened.asStateFlow()
    val isNextStep
        get() = uiState.value.isNextStep
    val username
        get() = uiState.value.username

    val avatar
        get() = uiState.value.avatar

    val email
        get() = uiState.value.email


    private fun onUsernameChange(newValue: String) {
        uiState.value = uiState.value.copy(username = newValue)
    }
    private fun onAvatarChange(newValue: String) {
        uiState.value = uiState.value.copy(avatar = newValue)
    }
    fun onEmailChange(newValue: String) {
        uiState.value = uiState.value.copy(email = newValue)
    }
    fun changeIsNextStep(isNextStep: Boolean) {
        uiState.value = uiState.value.copy(isNextStep = isNextStep)
    }
    fun openDrawer() {
        _drawerShouldBeOpened.value = true
    }

    fun resetOpenDrawerAction() {
        _drawerShouldBeOpened.value = false
    }

    fun changeEmail() {
        launchCatching {
            if (authenticationService.updateEmail(email)) {
                SnackbarManager.showMessage(R.string.update_email_success)
            } else {
                SnackbarManager.showMessage(R.string.update_email_fail)
            }
        }
    }

    private suspend fun getTokenAndConnectUser(username: String) {
        try {
            val tokenProvider = StreamTokenProvider(StreamTokenApi())
            val token = tokenProvider.getTokenProvider(username).loadToken()
            val user = User(id = username, name = username)
            val connectResult = client.connectUser(user = user, token = token)

            connectResult.enqueue { result ->
                if (result.isSuccess) {
                    Log.d("dat123456", "đăng nhập người dùng thành công")
                } else {
                    Log.d("dat12345", "đăng nhập người dùng thất bại")
                }
            }
        } catch (e: Exception) {
            Log.e("Error", "Lỗi khi lấy token và kết nối người dùng", e)
        }
    }

    fun createChannel(channelName: String, channelType: String = "messaging") {
        val trimmedChannelName = channelName.trim()
        val channelId = UUID.randomUUID().toString()

        viewModelScope.launch {
                if (trimmedChannelName.isEmpty()) {
                    SnackbarManager.showMessage(R.string.channelName_error)
                    return@launch
            }
        client.createChannel(
            channelType = channelType,
            channelId = channelId,
            memberIds = listOf(userid),
            extraData = mapOf(
                "name" to trimmedChannelName,
                "image" to ""
            )
        ).enqueue {result ->
            if (result.isSuccess) {
                viewModelScope.launch {
                    _createChannelEvent.emit(CreateChannelEvent.Success)
                    SnackbarManager.showMessage(R.string.createChannel_success)
                }
            } else {
                viewModelScope.launch {
                    SnackbarManager.showMessage(R.string.createChannel_fail)
                }
            }

        }
        }
    }

    fun onSignOutClick(restartApp: (String) -> Unit) {
        launchCatching {
            authenticationService.signOut()
            restartApp(LOGIN_SCREEN)
        }
    }

    sealed class CreateChannelEvent{
        data class Error(val error: String) : CreateChannelEvent()
        object Success: CreateChannelEvent()
    }


}