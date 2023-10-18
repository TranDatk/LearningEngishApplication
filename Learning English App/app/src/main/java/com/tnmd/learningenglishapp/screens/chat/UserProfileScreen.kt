package com.tnmd.learningenglishapp.screens.chat


import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import androidx.core.content.ContextCompat.startActivity
import androidx.core.content.FileProvider
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import coil.transform.CircleCropTransformation
import com.tnmd.learningenglishapp.R
import com.tnmd.learningenglishapp.activity.LoginActivity
import com.tnmd.learningenglishapp.activity.PayScreen
import com.tnmd.learningenglishapp.common.composable.EmailField
import com.tnmd.learningenglishapp.common.composable.UsernameField
import com.tnmd.learningenglishapp.common.ext.fieldModifier
import com.tnmd.learningenglishapp.common.snackbar.SnackbarManager
import com.tnmd.learningenglishapp.model.AccountLevel
import com.tnmd.learningenglishapp.screens.sign_up.createImageFile
import java.io.File
import java.text.SimpleDateFormat
import java.util.Date


@OptIn(ExperimentalMaterialApi::class)
@RequiresApi(Build.VERSION_CODES.P)
@Composable
fun UserProfileScreen(viewModal: ChannelListViewModal = hiltViewModel()) {
    var isDialogVisible by remember { mutableStateOf(false) }
    var isDialogVisible2 by remember { mutableStateOf(false) }
    var isDialogVisible3 by remember { mutableStateOf(false) }
    var isDialogVisible4 by remember { mutableStateOf(false) }
    var showConfirmationDialog by remember { mutableStateOf(false) }
    var showDialog by remember { mutableStateOf(false) }
    var selectedAccountLevel by remember { mutableStateOf<AccountLevel?>(null) }
    val accountLevels by viewModal.accountLevels.observeAsState()
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
            .background(MaterialTheme.colorScheme.background)

    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth(),
            contentAlignment = Alignment.CenterStart
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

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp),
            contentAlignment = Alignment.Center
        ) {
            NetworkImage(viewModal.avatar, null)
        }




        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Level: ${viewModal.level}",
                fontSize = 20.sp,
                textAlign = TextAlign.Center
            )
            Icon(imageVector = Icons.Default.Info, contentDescription = null, modifier = Modifier
                .size(24.dp)
                .clickable {
                    isDialogVisible4 = true
                })
        }
        Spacer(modifier = Modifier.height(10.dp))


        Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
        ) {
        Text(text = "Name: ${viewModal.username}", fontSize = 20.sp, textAlign = TextAlign.Center)
        Icon(imageVector = Icons.Default.Edit, contentDescription = null, modifier = Modifier
            .size(24.dp)
            .clickable {
                isDialogVisible2 = true
            })
    }
        Spacer(modifier = Modifier.height(10.dp))
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(text = "Email: ${viewModal.email}", fontSize = 20.sp, textAlign = TextAlign.Center)
            Icon(imageVector = Icons.Default.Edit, contentDescription = null, modifier = Modifier
                .size(24.dp)
                .clickable {
                    isDialogVisible = true
                })
        }
        Spacer(modifier = Modifier.height(10.dp))
        Button(
            onClick = {
                isDialogVisible3 = true
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(text = "Đổi ảnh đại diện")
        }

        Button(
            onClick = { showDialog = true },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(text = "Nâng cấp tài khoản")
        }
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

    if (isDialogVisible4) {
        AlertDialog(
            onDismissRequest = {
                // Đóng dialog khi người dùng nhấn ra ngoài dialog
                isDialogVisible4 = false
            },
            title = {
                Text(text = "Thông tin tài khoản")
            },
            text = {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()

                ) {
                    Text(
                        text = "Mô tả: ${viewModal.descriptionLevel}",
                        fontSize = 20.sp,
                        textAlign = TextAlign.Center
                    )
                    Text(
                        text = "Số ngày hết hạn: ${viewModal.validityPeriod}",
                        fontSize = 20.sp,
                        textAlign = TextAlign.Center
                    )
                }
            }
            ,
            confirmButton = {
                Button(
                    onClick = {
                        isDialogVisible4 = false
                    }
                ) {
                    Text(text = "Thoát")
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

    if (showDialog) {
        if (showConfirmationDialog) {
            AlertDialog(
                onDismissRequest = {
                    showDialog = false
                    showConfirmationDialog = false
                },
                title = {
                    Text(text = "Xác nhận nâng cấp tài khoản")
                },
                text = {
                    Text(text = "Bạn chắc chắn muốn nâng cấp tài khoản?")
                },
                confirmButton = {
                    Button(
                        onClick = {
                            selectedAccountLevel?.let { selectedLevel ->
                                // Thực hiện thanh toán Momo ở đây
                                viewModal.updateAccountLevel(selectedAccountLevel!!.id)
                                showDialog = false
                                showConfirmationDialog = false
                                val intent = Intent(context, PayScreen::class.java)
                                context.startActivity(intent)
                            }
                        }
                    ) {
                        Text(text = "Đồng ý")
                    }
                },
                dismissButton = {
                    Button(
                        onClick = {
                            showDialog = false
                            showConfirmationDialog = false
                        }
                    ) {
                        Text(text = "Hủy")
                    }
                }
            )
        } else {
            AlertDialog(
                onDismissRequest = {
                    showDialog = false
                },
                title = {
                    Text(text = "Nâng cấp tài khoản")
                },
                text = {
                    LazyColumn {
                        items(accountLevels.orEmpty()) { accountLevel ->
                            AccountLevelItem(accountLevel) { selectedAccountLevel = it }
                        }
                    }
                },
                confirmButton = {
                    Button(
                        onClick = {
                            showConfirmationDialog = true
                        }
                    ) {
                        Text(text = "Lưu")
                    }
                },
                dismissButton = {
                    Button(
                        onClick = {
                            showDialog = false
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
            .size(200.dp)
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

@Composable
fun AccountLevelItem(accountLevel: AccountLevel,onItemClick: (AccountLevel) -> Unit) {
    var isAlertDialogVisible by remember { mutableStateOf(false) }
    Box(
        modifier = Modifier
            .padding(8.dp)
            .fillMaxWidth()
            .height(50.dp)
            .background(MaterialTheme.colorScheme.background, shape = RoundedCornerShape(8.dp))

            .clickable {
                onItemClick(accountLevel)
            }
    ) {
        if (isAlertDialogVisible) {
            AlertDialog(
                onDismissRequest = {
                    isAlertDialogVisible = false
                },
                title = {
                    Text(text = "Level ${accountLevel.level}")
                },
                text = {
                    Column {
                        Text(text = "Description: ${accountLevel.descriptionLevel}")
                        Text(text = "Validity: ${accountLevel.validityPeriod}")
                        Text(text = "Price: 25.000$")
                    }
                },
                confirmButton = {
                }
            )
        }
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(8.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Hiển thị level và icon Info
            Row(modifier = Modifier
                .fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(text = "Level ${accountLevel.level}", fontWeight = FontWeight.Bold)
                Icon(imageVector = Icons.Default.Info, contentDescription = null, modifier = Modifier.clickable {
                    isAlertDialogVisible = true
                })
            }
        }

    }
}


