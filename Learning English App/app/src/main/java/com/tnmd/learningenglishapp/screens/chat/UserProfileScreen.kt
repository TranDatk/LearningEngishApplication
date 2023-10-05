package com.tnmd.learningenglishapp.screens.chat


import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.BitmapPainter
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.rememberAsyncImagePainter
import coil.compose.rememberImagePainter
import coil.request.ImageRequest
import coil.transform.CircleCropTransformation
import com.tnmd.learningenglishapp.common.composable.EmailField
import com.tnmd.learningenglishapp.common.ext.fieldModifier


@Composable
fun UserProfileScreen(viewModal: ChannelListViewModal = hiltViewModel()) {
    var isDialogVisible by remember { mutableStateOf(false) }
    val uiState by viewModal.uiState
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.CenterHorizontally)
        ) {
            IconButton(
                onClick = {
                    viewModal.changeIsNextStep(false)
                },
                modifier = Modifier.padding(8.dp)
            ) {
                Icon(imageVector = Icons.Default.ArrowBack, contentDescription = "Back")
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.CenterHorizontally)
                .padding(20.dp)
        ) {
            NetworkImage(viewModal.avatar,null)
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Hiển thị tên và địa chỉ email của người dùng
        Text(text = "Tên: ${viewModal.username}", fontSize = 20.sp, textAlign = TextAlign.Center)
        Spacer(modifier = Modifier.height(8.dp))
        Text(text = "Email: ${viewModal.email}", fontSize = 20.sp, textAlign = TextAlign.Center)

        Spacer(modifier = Modifier.height(16.dp))
        Text(text = "Tài khoản của bạn là tài khoản thường", fontSize = 20.sp, textAlign = TextAlign.Center)
        Button(
            onClick = {
                // Hiển thị dialog để nhập email hoặc tên mới
                // Thực hiện các hành động cần thiết khi người dùng nhấn nút "Lưu"
            },
            modifier = Modifier.align(Alignment.CenterHorizontally)
        ) {
            Text(text = "Nâng cấp tài khoản")
        }
        // Hiển thị nút sửa thông tin người dùng
        Button(
            onClick = {
                // Hiển thị dialog khi nút được nhấn
                isDialogVisible = true
            },
            modifier = Modifier.align(Alignment.CenterHorizontally)
        ) {
            Text(text = "Sửa thông tin email")
        }

        if (isDialogVisible) {
            AlertDialog(
                onDismissRequest = {
                    // Đóng dialog khi người dùng nhấn ra ngoài dialog
                    isDialogVisible = false
                },
                title = {
                    Text(text = "Chỉnh sửa thông tin người dùng")
                },
                text = {
                    EmailField(uiState.email, viewModal::onEmailChange, Modifier.fieldModifier())
                },
                confirmButton = {
                    Button(
                        onClick = {
                            viewModal.changeEmail()
                            isDialogVisible = false
                        }
                    ) {
                        Text(text = "Lưu")
                    }
                },
                dismissButton = {
                    Button(
                        onClick = {
                            // Đóng dialog khi người dùng nhấn nút "Hủy"
                            isDialogVisible = false
                        }
                    ) {
                        Text(text = "Hủy")
                    }
                }
            )
        }
    }
}
@Composable
fun NetworkImage(url: String, contentDescription: String?, modifier: Modifier = Modifier) {
    val painter = // Transform hình ảnh thành hình tròn
        rememberAsyncImagePainter(
            ImageRequest.Builder(LocalContext.current).data(data = url)
                .apply<ImageRequest.Builder>(block = fun ImageRequest.Builder.() {
                    transformations(CircleCropTransformation()) // Transform hình ảnh thành hình tròn
                }).build()
        )

    Image(
        painter = painter,
        contentDescription = contentDescription,
        modifier = modifier
            .size(100.dp)
            .clip(CircleShape)
            .border(1.dp, Color.Gray, CircleShape),
        contentScale = ContentScale.Crop
    )
}