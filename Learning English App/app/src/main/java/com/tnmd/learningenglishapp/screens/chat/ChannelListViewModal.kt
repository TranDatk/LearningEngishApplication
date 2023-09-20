package com.tnmd.learningenglishapp.screens.chat

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.tnmd.learningenglishapp.R
import com.tnmd.learningenglishapp.common.snackbar.SnackbarManager
import com.tnmd.learningenglishapp.data.StreamTokenApi
import com.tnmd.learningenglishapp.model.service.AuthenticationService
import com.tnmd.learningenglishapp.model.service.LogService
import com.tnmd.learningenglishapp.screens.LearningEnglishAppViewModel
import com.tnmd.learningenglishapp.screens.login.StreamTokenProvider
import dagger.hilt.android.lifecycle.HiltViewModel
import io.getstream.chat.android.client.ChatClient
import io.getstream.chat.android.client.api.models.QueryUsersRequest
import io.getstream.chat.android.client.models.Filters
import io.getstream.chat.android.client.models.User
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import java.util.UUID
import javax.inject.Inject

@HiltViewModel
class ChannelListViewModal @Inject constructor(
    private val client: ChatClient,
    private val authenticationService: AuthenticationService,
    logService: LogService,
) : LearningEnglishAppViewModel(logService){

    private val _createChannelEvent = MutableSharedFlow<CreateChannelEvent>()
    val createChannelEvent = _createChannelEvent.asSharedFlow()
    private val _users = MutableLiveData<List<User>>()

    val users: MutableLiveData<List<User>> = MutableLiveData() // Sử dụng MutableLiveData thay vì LiveData
    private val userid = authenticationService.currentUserId
    val userList: List<User>? = users.value
    init {
        viewModelScope.launch {
            getTokenAndConnectUser(userid)
            Log.d("token",userid)
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

    sealed class CreateChannelEvent{
        data class Error(val error: String) : CreateChannelEvent()
        object Success: CreateChannelEvent()
    }
    

}