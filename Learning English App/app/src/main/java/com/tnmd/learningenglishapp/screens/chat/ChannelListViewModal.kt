package com.tnmd.learningenglishapp.screens.chat

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tnmd.learningenglishapp.R
import com.tnmd.learningenglishapp.common.snackbar.SnackbarManager
import dagger.hilt.android.lifecycle.HiltViewModel
import io.getstream.chat.android.client.ChatClient
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import java.util.UUID
import javax.inject.Inject

@HiltViewModel
class ChannelListViewModal @Inject constructor(
    private val client: ChatClient
) : ViewModel(){

    private val _createChannelEvent = MutableSharedFlow<CreateChannelEvent>()
    val createChannelEvent = _createChannelEvent.asSharedFlow()

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
            memberIds = emptyList(),
            extraData = mapOf(
                "name" to trimmedChannelName,
                "image" to ""
            )
        ).enqueue {result ->
            if (result.isSuccess) {
                viewModelScope.launch {
                    _createChannelEvent.emit(CreateChannelEvent.Success)
                    SnackbarManager.showMessage(R.string.createChannel_fail)
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