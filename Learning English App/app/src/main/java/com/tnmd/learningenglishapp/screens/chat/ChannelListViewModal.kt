package com.tnmd.learningenglishapp.screens.chat

import android.net.Uri
import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.tnmd.learningenglishapp.LOGIN_SCREEN
import com.tnmd.learningenglishapp.R
import com.tnmd.learningenglishapp.common.snackbar.SnackbarManager
import com.tnmd.learningenglishapp.data_streamchat.StreamTokenApi
import com.tnmd.learningenglishapp.model.AccountLevel
import com.tnmd.learningenglishapp.model.service.AccountAccountLevelService
import com.tnmd.learningenglishapp.model.service.AccountLevelService
import com.tnmd.learningenglishapp.model.service.AuthenticationService
import com.tnmd.learningenglishapp.model.service.LearnerService
import com.tnmd.learningenglishapp.model.service.LogService
import com.tnmd.learningenglishapp.screens.LearningEnglishAppViewModel
import com.tnmd.learningenglishapp.screens.login.StreamTokenProvider
import dagger.hilt.android.lifecycle.HiltViewModel
import io.getstream.chat.android.client.ChatClient
import io.getstream.chat.android.client.models.User
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import org.json.JSONException
import org.json.JSONObject
import vn.momo.momo_partner.AppMoMoLib
import java.util.UUID
import javax.inject.Inject

@HiltViewModel
class ChannelListViewModal @Inject constructor(
    private val client: ChatClient,
    private val authenticationService: AuthenticationService,
    logService: LogService,
    private val learnerService: LearnerService,
    private val accountLevelService: AccountLevelService,
    private val accountAccountLevelService: AccountAccountLevelService
) : LearningEnglishAppViewModel(logService){
    var uiState = mutableStateOf(ScreenUiState())
        private set
    private val _createChannelEvent = MutableSharedFlow<CreateChannelEvent>()
    val createChannelEvent = _createChannelEvent.asSharedFlow()
    private val userid = authenticationService.currentUserId
    private val currentUser = authenticationService.currentUser
    private var amount: String = "10000"
    private var fee: String = "0"
    private var environment: Int = 0 // developer default
    private var merchantName: String = "Thanh toán đơn hàng"
    private var merchantCode: String = "MOMOO41P20220925" // cái này fen xóa này
    private var merchantNameLabel: String = "HotelRent"
    private var description: String = "nạp tiền vào tài khoản"
    private val _accountLevels = MutableLiveData<List<AccountLevel>?>(emptyList())

    val accountLevels: MutableLiveData<List<AccountLevel>?> get() = _accountLevels
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
            val accountLevelId = accountAccountLevelService.getAccountLevelId(userid)
            val level = accountLevelId?.let { accountLevelService.getLevelAccount(it) }
            val des = accountLevelId?.let { accountLevelService.getDescriptionLevel(it) }
            val validity = accountLevelId?.let { accountLevelService.getValidityPeriod(it) }
            loadAccountLevels()
            Log.d("dattesst1213", accountLevels.value.toString())
            onEmailChange(email)
            onUsernameChange(username)
            onAvatarChange(avatar)
            if (level != null && des != null && validity != null) {
                onLevelChange(level)
                onDescriptionChange(des)
                onValidityChange(validity)
            }
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

    val level
        get() = uiState.value.level


    val descriptionLevel
        get() = uiState.value.descriptionLevel


    val validityPeriod
        get() = uiState.value.validityPeriod

    fun onUsernameChange(newValue: String) {
        uiState.value = uiState.value.copy(username = newValue)
    }
    fun onLevelChange(newValue: String) {
        uiState.value = uiState.value.copy(level = newValue)
    }

    fun onDescriptionChange(newValue: String) {
        uiState.value = uiState.value.copy(descriptionLevel = newValue)
    }

    fun onValidityChange(newValue: Int) {
        uiState.value = uiState.value.copy(validityPeriod = newValue)
    }
    fun onAvatarChange(newValue: String) {
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

    fun updateAccountLevel(accountLevelId : String) {
        launchCatching {
            if (accountAccountLevelService.updateAccountLevelId(userid, accountLevelId)) {
                SnackbarManager.showMessage(R.string.update_level_success)
            } else {
                SnackbarManager.showMessage(R.string.update_level_fail)
            }
        }
    }
    private suspend fun loadAccountLevels() {
        val accountLevelsList = accountLevelService.getAllAccountLevels()
        _accountLevels.value = accountLevelsList
    }




    fun changeUserName() {
        launchCatching {
            if (learnerService.changeUsername(username)) {
                SnackbarManager.showMessage(R.string.update_username_success)
            } else {
                SnackbarManager.showMessage(R.string.update_username_fail)
            }
        }
    }
    fun updateAvatar(image: Uri?) {
        launchCatching {
            if (authenticationService.updateAndUploadImageToFirebase(userid,image)) {
                SnackbarManager.showMessage(R.string.update_avatar_success)
            } else {
                SnackbarManager.showMessage(R.string.update_avatar_fail)
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

