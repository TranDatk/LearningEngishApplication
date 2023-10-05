package com.tnmd.learningenglishapp.common

import android.content.Intent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.windowInsetsTopHeight
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.AlertDialog
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.WbSunny
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.rememberAsyncImagePainter
import com.tnmd.learningenglishapp.LOGIN_SCREEN
import com.tnmd.learningenglishapp.R
import com.tnmd.learningenglishapp.SETTINGS_SCREEN
import com.tnmd.learningenglishapp.USERPROFILE_SCREEN
import com.tnmd.learningenglishapp.activity.LoginActivity
import com.tnmd.learningenglishapp.common.ext.card
import com.tnmd.learningenglishapp.composable.DialogCancelButton
import com.tnmd.learningenglishapp.composable.DialogConfirmButton
import com.tnmd.learningenglishapp.composable.RegularCardEditor
import com.tnmd.learningenglishapp.model.service.AuthenticationService
import com.tnmd.learningenglishapp.screens.chat.ChannelListViewModal
import com.tnmd.learningenglishapp.screens.chat.UrlLauncher
import com.tnmd.learningenglishapp.screens.chat.UserProfileScreen
import com.tnmd.learningenglishapp.screens.login.LoginScreen

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun AppDrawer(
    viewModal: ChannelListViewModal = hiltViewModel(),
    onIconClicked: () -> Unit = {},
) {
    val uiState by viewModal.uiState
    val context = LocalContext.current
    var showWarningDialog by remember { mutableStateOf(false) }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        if (uiState.isNextStep == true) {
            UserProfileScreen(viewModal = viewModal)
        }
        Spacer(Modifier.windowInsetsTopHeight(WindowInsets.statusBars))
        DrawerHeader(clickAction = onIconClicked)
        DividerItem()
        DividerItem(modifier = Modifier.padding(horizontal = 28.dp))
        DrawerItemHeader("Author")
        ProfileItem(
            "CapTanDat (author)",
            urlToImageAuthor,
        ) {
            UrlLauncher().openUrl(context = context, urlToGithub)
        }
        ProfileItem(
            "TranDatk (author)",
            urlToImageAuthor,
        ) {
            UrlLauncher().openUrl(context = context, urlToGithub2)
        }
        DrawerItemHeader("Settings")
        ProfileItem(
            "Thông tin cá nhân",
            null,
        ) {
            viewModal.changeIsNextStep(true)
        }
        ProfileItem(
            "Đăng xuất",
            null,
        ) {
            showWarningDialog = true
        }
        if (showWarningDialog) {
            AlertDialog(
                title = { androidx.compose.material.Text(stringResource(R.string.sign_out_title)) },
                text = { androidx.compose.material.Text(stringResource(R.string.sign_out_description)) },
                dismissButton = { DialogCancelButton(R.string.cancel) { showWarningDialog = false } },
                confirmButton = {
                    DialogConfirmButton(R.string.sign_out) {
                        viewModal.onSignOutClick {
                            viewModal.onSignOutClick { LOGIN_SCREEN }
                            val intent = Intent(context, LoginActivity::class.java)
                            context.startActivity(intent)
                        }
                        showWarningDialog = false
                    }
                },
                onDismissRequest = { showWarningDialog = false }
            )
        }
    }
}

@Composable
private fun DrawerHeader(
    clickAction: () -> Unit = {}
) {
    val paddingSizeModifier = Modifier
        .padding(start = 16.dp, top = 16.dp, bottom = 16.dp)
        .size(34.dp)

    Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.SpaceBetween) {
        Row(
            modifier = Modifier
                .padding(16.dp)
                .weight(1f), verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                painter = rememberAsyncImagePainter(urlToImageAppIcon),
                modifier = paddingSizeModifier.then(Modifier.clip(RoundedCornerShape(6.dp))),
                contentScale = ContentScale.Crop,
                contentDescription = null
            )
            Column(modifier = Modifier.padding(horizontal = 12.dp)) {
                Text(
                    "Ứng dụng học tiếng anh",
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.secondary,
                )
                Text(
                    "CapDat & TranDatk",
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Normal,
                    color = Color.Black,
                )
            }
        }

        IconButton(
            onClick = {
                clickAction.invoke()
            },
        ) {
            Icon(
                Icons.Filled.Settings,
                "SettingIcon",
                modifier = Modifier.size(26.dp),
                tint = MaterialTheme.colorScheme.primary,
            )
        }
    }
}

@Composable
private fun DrawerItemHeader(text: String) {
    Box(
        modifier = Modifier
            .heightIn(min = 52.dp)
            .padding(horizontal = 28.dp),
        contentAlignment = Alignment.CenterStart
    ) {
        Text(
            text,
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}


@Composable
private fun ProfileItem(text: String, urlToImage: String?, onProfileClicked: () -> Unit) {
    Row(
        modifier = Modifier
            .height(56.dp)
            .fillMaxWidth()
            .padding(horizontal = 12.dp)
            .clip(CircleShape)
            .clickable(onClick = onProfileClicked),
        verticalAlignment = Alignment.CenterVertically
    ) {
        val paddingSizeModifier = Modifier
            .padding(start = 16.dp, top = 16.dp, bottom = 16.dp)
            .size(24.dp)
        if (urlToImage != null) {
            Image(
                painter = rememberAsyncImagePainter(urlToImage),
                modifier = paddingSizeModifier.then(Modifier.clip(CircleShape)),
                contentScale = ContentScale.Crop,
                contentDescription = null
            )
        } else {
            Spacer(modifier = paddingSizeModifier)
        }
        Text(
            text,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurface,
            modifier = Modifier.padding(start = 12.dp)
        )
    }
}

@Composable
fun DividerItem(modifier: Modifier = Modifier) {
    Divider(
        modifier = modifier,
        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.12f)
    )
}


@ExperimentalMaterialApi
@Composable
private fun SignOutCard(signOut: () -> Unit) {


}