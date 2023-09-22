package com.tnmd.learningenglishapp.common

import android.annotation.SuppressLint
import androidx.compose.material3.DrawerState
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.hilt.navigation.compose.hiltViewModel
import com.tnmd.learningenglishapp.ui.theme.BackGroundColor
import io.getstream.chat.android.ui.channel.list.viewmodel.ChannelListViewModel
import kotlinx.coroutines.launch

@SuppressLint("CoroutineCreationDuringComposition")
@Composable
fun AppScaffold(
    drawerState: DrawerState = rememberDrawerState(initialValue = DrawerValue.Open),
    onIconClicked: () -> Unit = {},
    viewModel: ChannelListViewModel= hiltViewModel(),
    content: @Composable () -> Unit,
) {
    val scope = rememberCoroutineScope()

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            ModalDrawerSheet(drawerContainerColor = BackGroundColor) {
                AppDrawer(
                    onIconClicked = onIconClicked,
                )
            }
        },
        content = content
    )

}