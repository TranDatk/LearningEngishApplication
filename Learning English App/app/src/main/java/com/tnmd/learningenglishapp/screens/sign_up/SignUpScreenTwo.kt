package com.tnmd.learningenglishapp.screens.sign_up;


import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable;
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.hilt.navigation.compose.hiltViewModel
import com.tnmd.learningenglishapp.R
import com.tnmd.learningenglishapp.activity.LoginActivity
import com.tnmd.learningenglishapp.activity.MainActivity
import com.tnmd.learningenglishapp.common.ext.basicButton
import com.tnmd.learningenglishapp.common.snackbar.SnackbarManager
import com.tnmd.learningenglishapp.composable.BasicButton
import java.io.File
import java.text.SimpleDateFormat
import java.util.Date

@RequiresApi(Build.VERSION_CODES.P)
@Composable
fun SignUpScreenTwo(
    modifier: Modifier = Modifier,
    viewModel: SignUpViewModel = hiltViewModel()
) {

    val context = LocalContext.current
    @OptIn(ExperimentalMaterialApi::class)
    (LaunchedEffect(viewModel.loginEvent) {
        viewModel.loginEvent.collect { event ->
            when (event) {
                is SignUpViewModel.LogInEvent.Success -> {
                    val intent = Intent(context, MainActivity::class.java)
                    context.startActivity(intent)
                }
                is SignUpViewModel.LogInEvent.ErrorLogIn -> {
                    val intent = Intent(context, LoginActivity::class.java)
                    context.startActivity(intent)
                }
            }
        }
    })


    var imageUri by remember { mutableStateOf<Uri?>(null) }
    val bitmap = remember { mutableStateOf<Bitmap?>(null) }

    val galleryLauncher =
        rememberLauncherForActivityResult(contract = ActivityResultContracts.GetContent()) { uri: Uri? ->
            imageUri = uri
        }

    val cameraLauncher =
        rememberLauncherForActivityResult(ActivityResultContracts.TakePicture()) { success: Boolean ->
            if (success) {
                imageUri?.let { uri ->
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
            SnackbarManager.showMessage(R.string.permission_success)
            cameraLauncher.launch(imageUri)
        } else {
            SnackbarManager.showMessage(R.string.permission_fail)
        }
    }

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        imageUri?.let {
            if (Build.VERSION.SDK_INT < 28) {
                bitmap.value = MediaStore.Images.Media.getBitmap(
                    context.contentResolver,
                    it
                )
            } else {
                val source = ImageDecoder.createSource(
                    context.contentResolver,
                    it
                )
                bitmap.value = ImageDecoder.decodeBitmap(source)
            }

            bitmap.value?.let { btm ->
                Image(
                    bitmap = btm.asImageBitmap(),
                    contentDescription = null,
                    modifier = Modifier
                            .size(400.dp)
                            .padding(20.dp)
                )
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            Button(onClick = { galleryLauncher.launch("image/*") }) {
                Text(text = "Pick Image")
            }

            Button(onClick = {
                val permissionCheckResult =
                    ContextCompat.checkSelfPermission(
                        context,
                        Manifest.permission.CAMERA
                    )

                if (permissionCheckResult == PackageManager.PERMISSION_GRANTED) {
                    val file = context.createImageFile()
                    imageUri = FileProvider.getUriForFile(
                        context,
                        context.packageName + ".provider",
                        file
                    )
                    cameraLauncher.launch(imageUri)
                } else {
                    permissionLauncher.launch(Manifest.permission.CAMERA)
                }
            }) {
                Text(text = "Capture Image")
            }
        }
        BasicButton(R.string.complete_sign, Modifier.basicButton()) {
            if (imageUri != null) {
                viewModel.onSignUp(imageUri!!)
            } else {
                viewModel.onSignUp(null)
            }
    }
    }

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
