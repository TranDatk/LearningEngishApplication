package com.tnmd.learningenglishapp.screens.login

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import com.tnmd.learningenglishapp.R.string as AppText
import com.tnmd.learningenglishapp.common.composable.BasicToolbar
import com.tnmd.learningenglishapp.common.composable.EmailField
import com.tnmd.learningenglishapp.common.composable.PasswordField
import com.tnmd.learningenglishapp.common.ext.basicButton
import com.tnmd.learningenglishapp.common.ext.fieldModifier
import com.tnmd.learningenglishapp.common.ext.textButton
import com.tnmd.learningenglishapp.composable.BasicButton
import com.tnmd.learningenglishapp.composable.BasicTextButton

@Composable
fun LoginScreen(
  openAndPopUp: (String, String) -> Unit,
  modifier: Modifier = Modifier,
  viewModel: LoginViewModel = hiltViewModel()
) {
  val uiState by viewModel.uiState

  BasicToolbar(AppText.login_details)

  Column(
    modifier = modifier.fillMaxWidth().fillMaxHeight().verticalScroll(rememberScrollState()),
    verticalArrangement = Arrangement.Center,
    horizontalAlignment = Alignment.CenterHorizontally
  ) {
    EmailField(uiState.email, viewModel::onEmailChange, Modifier.fieldModifier())
    PasswordField(uiState.password, viewModel::onPasswordChange, Modifier.fieldModifier())

    BasicButton(AppText.sign_in, Modifier.basicButton()) { viewModel.onSignInClick(openAndPopUp) }

    BasicTextButton(AppText.forgot_password, Modifier.textButton()) {
      viewModel.onForgotPasswordClick()
    }
  }
}