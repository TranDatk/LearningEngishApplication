package com.tnmd.learningenglishapp.screens.chat

import android.annotation.SuppressLint
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import io.getstream.chat.android.compose.ui.theme.ChatTheme
import io.getstream.chat.android.compose.ui.theme.StreamShapes
import io.getstream.chat.android.compose.viewmodel.messages.AttachmentsPickerViewModel
import io.getstream.chat.android.compose.viewmodel.messages.MessageComposerViewModel
import io.getstream.chat.android.compose.viewmodel.messages.MessageListViewModel
import io.getstream.chat.android.compose.viewmodel.messages.MessagesViewModelFactory
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalContext
import io.getstream.chat.android.common.state.MessageMode
import io.getstream.chat.android.compose.ui.messages.attachments.AttachmentsPicker
import io.getstream.chat.android.compose.ui.messages.list.MessageList
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material.Scaffold
import androidx.compose.ui.Alignment
import io.getstream.chat.android.compose.state.messages.SelectedMessageOptionsState
import io.getstream.chat.android.compose.state.messages.SelectedMessageReactionsState
import io.getstream.chat.android.compose.ui.components.messageoptions.defaultMessageOptionsState
import io.getstream.chat.android.compose.ui.components.selectedmessage.SelectedMessageMenu
import io.getstream.chat.android.compose.ui.components.selectedmessage.SelectedReactionsMenu
import io.getstream.chat.android.compose.ui.messages.composer.MessageComposer
import androidx.activity.viewModels
import androidx.lifecycle.viewmodel.compose.viewModel

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
@ExperimentalMaterialApi
fun MessageScreen(
    channelName: String,
    modifier: Modifier = Modifier,
    
)
{
    val context = LocalContext.current

    val factory by lazy {
        MessagesViewModelFactory(
            context = context,
            channelId = channelName,
        )
    }
    val listViewModel: MessageListViewModel = viewModel(factory = factory)
    val attachmentsPickerViewModel: AttachmentsPickerViewModel = viewModel(factory = factory)
    val composerViewModel: MessageComposerViewModel = viewModel(factory = factory)


    ChatTheme(
        shapes = StreamShapes.defaultShapes().copy(
            avatar = RoundedCornerShape(8.dp),
            attachment = RoundedCornerShape(16.dp),
            myMessageBubble = RoundedCornerShape(16.dp),
            otherMessageBubble = RoundedCornerShape(16.dp),
            inputField = RoundedCornerShape(16.dp)
        )
    ) {
        val isShowingAttachments = attachmentsPickerViewModel.isShowingAttachments
        val selectedMessageState = listViewModel.currentMessagesState.selectedMessageState
        val user by listViewModel.user.collectAsState()

        Box(modifier = Modifier.fillMaxSize()) { // 2 - Define the root
            Scaffold(
                modifier = Modifier.fillMaxSize(),
                bottomBar = {
                    MessageComposer( // 3 - Add a composer
                        composerViewModel,
                        onAttachmentsClick = {
                            attachmentsPickerViewModel.changeAttachmentState(true)
                        }
                    )
                }
            ) {
                MessageList( // 4 - Build the MessageList and connect the actions
                    modifier = Modifier
                        .background(ChatTheme.colors.appBackground)
                        .padding(it)
                        .fillMaxSize(),
                    viewModel = listViewModel,
                    onThreadClick = { message ->
                        composerViewModel.setMessageMode(MessageMode.MessageThread(message))
                        listViewModel.openMessageThread(message)
                    }
                )
            }

            // 5 - Show attachments when necessary
            if (isShowingAttachments) {
                AttachmentsPicker(
                    attachmentsPickerViewModel = attachmentsPickerViewModel,
                    modifier = Modifier
                        .align(Alignment.BottomCenter)
                        .height(350.dp),
                    onAttachmentsSelected = { attachments ->
                        attachmentsPickerViewModel.changeAttachmentState(false)
                        composerViewModel.addSelectedAttachments(attachments)
                    },
                    onDismiss = {
                        attachmentsPickerViewModel.changeAttachmentState(false)
                        attachmentsPickerViewModel.dismissAttachments()
                    }
                )
            }

            // 6 - Show the overlay if we've selected a message
            if (selectedMessageState != null) {
                val selectedMessage = selectedMessageState.message
                if (selectedMessageState is SelectedMessageOptionsState) {
                    SelectedMessageMenu(
                        modifier = Modifier
                            .align(Alignment.Center)
                            .padding(horizontal = 20.dp)
                            .wrapContentSize(),
                        shape = ChatTheme.shapes.attachment,
                        messageOptions = defaultMessageOptionsState(
                            selectedMessage,
                            user,
                            listViewModel.isInThread,
                            selectedMessageState.ownCapabilities
                        ),
                        message = selectedMessage,
                        onMessageAction = { action ->
                            composerViewModel.performMessageAction(action)
                            listViewModel.performMessageAction(action)
                        },
                        onShowMoreReactionsSelected = {
                            listViewModel.selectExtendedReactions(selectedMessage)
                        },
                        onDismiss = { listViewModel.removeOverlay() },
                        ownCapabilities = selectedMessageState.ownCapabilities
                    )
                } else if (selectedMessageState is SelectedMessageReactionsState) {
                    SelectedReactionsMenu(
                        modifier = Modifier
                            .align(Alignment.Center)
                            .padding(horizontal = 20.dp)
                            .wrapContentSize(),
                        shape = ChatTheme.shapes.attachment,
                        message = selectedMessage,
                        currentUser = user,
                        onMessageAction = { action ->
                            composerViewModel.performMessageAction(action)
                            listViewModel.performMessageAction(action)
                        },
                        onShowMoreReactionsSelected = {
                            listViewModel.selectExtendedReactions(selectedMessage)
                        },
                        onDismiss = { listViewModel.removeOverlay() },
                        ownCapabilities = selectedMessageState.ownCapabilities
                    )
                }
            }
        }
    }
}



