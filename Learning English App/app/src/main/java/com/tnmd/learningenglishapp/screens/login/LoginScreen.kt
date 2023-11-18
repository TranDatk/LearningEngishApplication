package com.tnmd.learningenglishapp.screens.login

import android.content.Intent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.tnmd.learningenglishapp.activity.LoginActivity
import com.tnmd.learningenglishapp.activity.MainActivity
import com.tnmd.learningenglishapp.R.string as AppText
import com.tnmd.learningenglishapp.common.composable.BasicToolbar
import com.tnmd.learningenglishapp.common.composable.EmailField
import com.tnmd.learningenglishapp.common.composable.PasswordField
import com.tnmd.learningenglishapp.common.ext.basicButton
import com.tnmd.learningenglishapp.common.ext.fieldModifier
import com.tnmd.learningenglishapp.common.ext.spacer
import com.tnmd.learningenglishapp.common.ext.textButton
import com.tnmd.learningenglishapp.common.composable.BasicButton
import com.tnmd.learningenglishapp.common.composable.BasicTextButton



@Composable
fun LoginScreen(
  openAndPopUp: (String, String) -> Unit,
  modifier: Modifier = Modifier,
  viewModel: LoginViewModel = hiltViewModel()
) {
  val uiState by viewModel.uiState

  val context = LocalContext.current

  var showProcess: Boolean by remember {
    mutableStateOf(false)
  }

  LaunchedEffect(viewModel.loadingState) {
    viewModel.loadingState.observeForever { uiLoadingState ->
      showProcess = when (uiLoadingState) {
        is LoginViewModel.UiLoadingState.Loading -> true
        is LoginViewModel.UiLoadingState.NotLoading -> false
      }
    }
  }

  // Collect events from the loginEvent flow
  @OptIn(ExperimentalMaterialApi::class)
  LaunchedEffect(viewModel.loginEvent) {
    viewModel.loginEvent.collect { event ->
      when (event) {
        is LoginViewModel.LogInEvent.Success -> {
          val intent = Intent(context, MainActivity::class.java)
          context.startActivity(intent)
        }
        is LoginViewModel.LogInEvent.ErrorLogIn -> {
          val intent = Intent(context, LoginActivity::class.java)
          context.startActivity(intent)
        }
      }
    }
  }

  BasicToolbar(AppText.login_details)
  Column(
    modifier = modifier
      .fillMaxWidth()
      .fillMaxHeight()
      .verticalScroll(rememberScrollState()),
    verticalArrangement = Arrangement.Center,
    horizontalAlignment = Alignment.CenterHorizontally
  ) {
    EmailField(uiState.email, viewModel::onEmailChange, Modifier.fieldModifier())
    Spacer(modifier = Modifier.spacer())
    PasswordField(uiState.password, viewModel::onPasswordChange, Modifier.fieldModifier())
    Spacer(modifier = Modifier.spacer())
    Spacer(modifier = Modifier.spacer())
    BasicButton(AppText.sign_in, Modifier.basicButton()) {
      viewModel.onSignInClick(openAndPopUp)
     }
    BasicTextButton(AppText.forgot_password, Modifier.textButton()) {
      viewModel.onForgotPasswordClick()
    }


    if (showProcess) {
      CircularProgressIndicator(
        modifier = Modifier.size(50.dp),
        strokeWidth = 4.dp
      )
    }

  }
}