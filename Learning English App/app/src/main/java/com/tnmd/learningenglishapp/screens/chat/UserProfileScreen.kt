package com.tnmd.learningenglishapp.screens.chat


import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
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
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import coil.transform.CircleCropTransformation
import com.tnmd.learningenglishapp.R
import com.tnmd.learningenglishapp.common.composable.EmailField
import com.tnmd.learningenglishapp.common.composable.UsernameField
import com.tnmd.learningenglishapp.common.ext.fieldModifier
import com.tnmd.learningenglishapp.common.snackbar.SnackbarManager
import com.tnmd.learningenglishapp.screens.sign_up.createImageFile
import java.io.File
import java.text.SimpleDateFormat
import java.util.Date


@RequiresApi(Build.VERSION_CODES.P)
@Composable
fun UserProfileScreen(viewModal: ChannelListViewModal = hiltViewModel()) {
    var isDialogVisible by remember { mutableStateOf(false) }
    var isDialogVisible2 by remember { mutableStateOf(false) }
    var isDialogVisible3 by remember { mutableStateOf(false) }
    val uiState by viewModal.uiState

    val context = LocalContext.current ?: return

    val imageUri = remember { mutableStateOf<Uri?>(null) }

    val bitmap = remember { mutableStateOf<Bitmap?>(null) }
    val hasCameraPermission = remember { mutableStateOf(false) }
    val galleryLauncher =
        rememberLauncherForActivityResult(contract = ActivityResultContracts.GetContent()) { uri: Uri? ->
            imageUri.value = uri
        }

    val cameraLauncher =
        rememberLauncherForActivityResult(ActivityResultContracts.TakePicture()) { success: Boolean ->
            if (success) {
                imageUri.value?.let { uri ->
                    val source = ImageDecoder.createSource(
                        context.contentResolver,
                        uri
                    )
                    bitmap.value = ImageDecoder.decodeBitmap(source)
                }
            }
        }

    val permissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted: Boolean ->
        if (isGranted) {
            // Quyền CAMERA đã được cấp, bạn có thể sử dụng cameraLauncher ở đây
            hasCameraPermission.value = true
            val cameraPermission = ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.CAMERA
            )
            if (cameraPermission == PackageManager.PERMISSION_GRANTED) {
                val file = context.createImageFile()
                imageUri.value = FileProvider.getUriForFile(
                    context,
                    "${context.packageName}.provider",
                    file
                )
                cameraLauncher.launch(imageUri.value)
            } else {
                // Xử lý trường hợp CAMERA permission không được cấp
                SnackbarManager.showMessage(R.string.permission_fail)
            }
        } else {
            hasCameraPermission.value = false
            // Xử lý trường hợp người dùng từ chối cấp quyền CAMERA
            SnackbarManager.showMessage(R.string.permission_fail)
        }
    }
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
        // Hiển thị nút sửa thông tin người dùng
        Button(
            onClick = {
                isDialogVisible = true
            },
            modifier = Modifier.align(Alignment.CenterHorizontally)
        ) {
            Text(text = "Sửa thông tin email")
        }

        Button(
            onClick = {
                isDialogVisible2 = true
            },
            modifier = Modifier.align(Alignment.CenterHorizontally)
        ) {
            Text(text = "Sửa thông tin tên người dùng")
        }


        Button(
            onClick = {
                isDialogVisible3 = true
            },
            modifier = Modifier.align(Alignment.CenterHorizontally)
        ) {
            Text(text = "Đổi ảnh đại diện")
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

        if (isDialogVisible2) {
            AlertDialog(
                onDismissRequest = {
                    // Đóng dialog khi người dùng nhấn ra ngoài dialog
                    isDialogVisible2 = false
                },
                title = {
                    Text(text = "Chỉnh sửa thông tin người dùng")
                },
                text = {
                    UsernameField(uiState.username, viewModal::onUsernameChange , Modifier.fieldModifier())
                },
                confirmButton = {
                    Button(
                        onClick = {
                            viewModal.changeUserName()
                            isDialogVisible2 = false
                        }
                    ) {
                        Text(text = "Lưu")
                    }
                },
                dismissButton = {
                    Button(
                        onClick = {
                            // Đóng dialog khi người dùng nhấn nút "Hủy"
                            isDialogVisible2 = false
                        }
                    ) {
                        Text(text = "Hủy")
                    }
                }
            )
        }

        if (isDialogVisible3) {
            AlertDialog(
                onDismissRequest = {
                    isDialogVisible3 = false
                },
                title = {
                    Text(text = "Chỉnh sửa thông tin người dùng")
                },
                text = {
                    Column(
                        modifier = Modifier.fillMaxSize(),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        // Button to open the photo library
                        Column(
                            modifier = Modifier.fillMaxSize(),
                            verticalArrangement = Arrangement.Center,
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            // Hiển thị hình ảnh đã chọn
                            imageUri.value?.let { uri ->
                                Image(
                                    painter = rememberAsyncImagePainter(model = uri),
                                    contentDescription = null,
                                    modifier = Modifier
                                        .size(400.dp)
                                        .padding(20.dp)
                                )
                            }

                            Spacer(modifier = Modifier.height(12.dp))

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceEvenly
                            ) {
                                // Button để chọn hình ảnh từ thư viện
                                Button(onClick = { galleryLauncher.launch("image/*") }) {
                                    Text(text = "Pick Image")
                                }
                                Button(onClick = {
                                    if (hasCameraPermission.value) {
                                        // Quyền CAMERA đã được cấp, bạn có thể sử dụng cameraLauncher ở đây
                                        imageUri.value?.let {
                                            cameraLauncher.launch(it)
                                        }
                                    } else {
                                        // Quyền CAMERA chưa được cấp, gửi yêu cầu cấp quyền
                                        permissionLauncher.launch(Manifest.permission.CAMERA)
                                    }
                                }) {
                                    Text(text = "Capture Image")
                                }
                            }
                        }
                    }
                }
               ,
                confirmButton = {
                    Button(
                        onClick = {
                            isDialogVisible3 = false
                            if (imageUri.value != null) {
                                viewModal.updateAvatar(imageUri.value)
                            }
                            else {
                                SnackbarManager.showMessage(R.string.not_image)
                            }
                        }
                    ) {
                        Text(text = "Lưu")
                    }
                },
                dismissButton = {
                    Button(
                        onClick = {
                            // Đóng dialog khi người dùng nhấn nút "Hủy"
                            isDialogVisible3 = false
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

fun Context.createImageFile(): File {
    val timeStamp = SimpleDateFormat("yyyy_MM_dd_HH:mm:ss").format(Date())
    val imageFileName = "JPEG_" + timeStamp + "_"
    val image = File.createTempFile(
        imageFileName,
        ".jpg",
        externalCacheDir
    )
    return image
}