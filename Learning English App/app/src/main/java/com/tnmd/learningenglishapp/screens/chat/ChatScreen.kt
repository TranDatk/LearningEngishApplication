package com.tnmd.learningenglishapp.screens.chat

import android.annotation.SuppressLint
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.AlertDialog
import androidx.compose.material.Button
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.material3.Divider
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.tnmd.learningenglishapp.common.AppScaffold
import io.getstream.chat.android.compose.ui.channels.ChannelsScreen
import io.getstream.chat.android.compose.ui.theme.ChatTheme
import kotlinx.coroutines.launch


@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
@ExperimentalMaterialApi
fun ChatScreen(
    openScreen: (String) -> Unit,
    viewModel: ChannelListViewModal = hiltViewModel()
) {

    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val drawerOpen by viewModel.drawerShouldBeOpened.collectAsState()

    if (drawerOpen) {
        LaunchedEffect(Unit) {
            try {
                drawerState.open()
            } finally {
                viewModel.resetOpenDrawerAction()
            }
        }
    }

    val scope = rememberCoroutineScope()
    val focusManager = LocalFocusManager.current

    BackHandler {
        if (drawerState.isOpen) {
            scope.launch {
                drawerState.close()
            }
        } else {
            focusManager.clearFocus()
        }
    }

    AppScaffold(
        drawerState = drawerState,
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
        ) {
            Divider()

            ChatTheme {

                var showDialog: Boolean by remember {
                    mutableStateOf(false)
                }

                if (showDialog) {
                    CreateChannelDialog(
                        dismiss = {channelName ->
                            run {
                                viewModel.createChannel(channelName)
                                showDialog = false
                            }
                        }
                    )
                }
                ChannelsScreen(

                    title= "Channel List",
                    isShowingHeader = true,
                    onItemClick = { channel -> openScreen(channel.cid)
                    },
                    onHeaderActionClick = {
                        showDialog = true
                    },
                    onHeaderAvatarClick = {

                    }
                )
            }
        }
    }

}

@Composable
 private fun CreateChannelDialog(dismiss: (String) -> Unit) {
     var channelName by remember {
         mutableStateOf("")
     }
    
    AlertDialog(
        onDismissRequest = { dismiss(channelName) },
        title = {
            Text(text = "Nhập tên nhóm")
        },
        text = {
            TextField(value = channelName, onValueChange = {channelName = it})
        },
        buttons = {
            Row(
                modifier = Modifier.padding(all = 8.dp),
                horizontalArrangement = Arrangement.Center
            ) {
                Button(
                    onClick = { dismiss(channelName)},
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(text = "Tạo nhóm chat")
                }
            }
        }
    )
 }
