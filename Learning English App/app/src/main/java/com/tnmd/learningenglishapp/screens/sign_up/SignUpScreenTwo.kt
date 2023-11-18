package com.tnmd.learningenglishapp.screens.sign_up


import android.Manifest
import android.annotation.SuppressLint
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
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.rememberAsyncImagePainter
import com.tnmd.learningenglishapp.R
import com.tnmd.learningenglishapp.common.ext.basicButton
import com.tnmd.learningenglishapp.common.snackbar.SnackbarManager
import com.tnmd.learningenglishapp.common.composable.BasicButton
import com.tnmd.learningenglishapp.common.composable.BasicTextButton
import com.tnmd.learningenglishapp.common.ext.textButton
import java.io.File
import java.text.SimpleDateFormat
import java.util.Date

@RequiresApi(Build.VERSION_CODES.P)
@Composable
fun SignUpScreenTwo(
    openScreen: (String) -> Unit,
    viewModel: SignUpViewModel = hiltViewModel()
) {
    val context = LocalContext.current

    val imageUri = remember { mutableStateOf<Uri?>(null) }

    val bitmap = remember { mutableStateOf<Bitmap?>(null) }
    val hasCameraPermission = remember { mutableStateOf(false) }

    val galleryLauncher = rememberLauncherForActivityResult(contract = ActivityResultContracts.GetContent()) { uri: Uri? ->
        imageUri.value = uri
    }
    val cameraLauncher = rememberLauncherForActivityResult(ActivityResultContracts.TakePicture()) { success: Boolean ->
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
                    .border(
                        width = 1.dp,
                        color = Color.Black,
                        shape = RoundedCornerShape(
                            15.dp
                        )
                    )
            )
        }

        Spacer(modifier = Modifier.height(12.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            // Button để chọn hình ảnh từ thư viện
            Button(onClick = { galleryLauncher.launch("image/*") },
             ) {
                Text(text = "Chọn từ thư viện",color = Color.Black)
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
            }
                ) {
                Text(text = "Chụp ảnh", color = Color.Black)
            }
        }
        Spacer(modifier = Modifier.height(12.dp))
        // Button để hoàn thành đăng ký
        BasicButton(R.string.complete_sign, Modifier.basicButton()) {
            if (imageUri.value != null) {
                viewModel.onSignUp(imageUri.value!!)
                viewModel.onLogin(openScreen)
            } else {
                viewModel.onSignUp(null)
            }
        }
        Spacer(modifier = Modifier.height(12.dp))
        Text(text = "Bạn có thể bỏ qua bước này!!", color = Color.Black)
    }
}

@SuppressLint("SimpleDateFormat")
fun Context.createImageFile(): File {
    val timeStamp = SimpleDateFormat("yyyy_MM_dd_HH:mm:ss").format(Date())
    val imageFileName = "JPEG_" + timeStamp + "_"
    return File.createTempFile(
        imageFileName,
        ".jpg",
        externalCacheDir
    )
}
