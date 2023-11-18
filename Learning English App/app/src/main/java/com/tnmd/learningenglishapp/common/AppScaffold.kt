package com.tnmd.learningenglishapp.common

import android.annotation.SuppressLint
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.material3.DrawerState
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import com.tnmd.learningenglishapp.ui.theme.BackGroundColor

@RequiresApi(Build.VERSION_CODES.P)
@SuppressLint("CoroutineCreationDuringComposition")
@Composable
fun AppScaffold(
    drawerState: DrawerState = rememberDrawerState(initialValue = DrawerValue.Open),
    onIconClicked: () -> Unit = {},
    content: @Composable () -> Unit,
) {
    rememberCoroutineScope()

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