/*
Copyright 2022 Google LLC

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

    https://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
 */

package com.tnmd.learningenglishapp.screens.sign_up

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.tnmd.learningenglishapp.R.string as AppText
import com.tnmd.learningenglishapp.common.composable.BasicToolbar
import com.tnmd.learningenglishapp.common.composable.EmailField
import com.tnmd.learningenglishapp.common.composable.PasswordField
import com.tnmd.learningenglishapp.common.composable.RepeatPasswordField
import com.tnmd.learningenglishapp.common.composable.UsernameField
import com.tnmd.learningenglishapp.common.ext.basicButton
import com.tnmd.learningenglishapp.common.ext.fieldModifier
import com.tnmd.learningenglishapp.composable.BasicButton
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import com.tnmd.learningenglishapp.SIGN_UP_SCREEN_TWO
import com.tnmd.learningenglishapp.common.snackbar.SnackbarManager

@Composable
fun SignUpScreen(
  openScreen: (String) -> Unit,
  modifier: Modifier = Modifier,
  viewModel: SignUpViewModel = hiltViewModel()
) {
  val uiState by viewModel.uiState
  val fieldModifier = Modifier.fieldModifier()

  // Biến để lưu giới tính được chọn, mặc định là "Nam"
  var selectedGender by remember { mutableStateOf("Nam") }




  BasicToolbar(AppText.create_account)

  Column(
    modifier = modifier
      .fillMaxWidth()
      .fillMaxHeight()
      .verticalScroll(rememberScrollState()),
    verticalArrangement = Arrangement.Center,
    horizontalAlignment = Alignment.CenterHorizontally
  ) {
    EmailField(uiState.email, viewModel::onEmailChange, fieldModifier)
    UsernameField(uiState.username, viewModel::onUsernameChange, fieldModifier)
    PasswordField(uiState.password, viewModel::onPasswordChange, fieldModifier)
    RepeatPasswordField(uiState.repeatPassword, viewModel::onRepeatPasswordChange, fieldModifier)

    RadioButtonGender(
      selectedGender = selectedGender,
      onGenderSelected = { gender -> selectedGender = gender },
      modifier = Modifier.padding(horizontal =20.dp)
    )

    BasicButton(AppText.next_step, Modifier.basicButton()) {
        viewModel.onNextStep(openScreen,selectedGender)
    }
  }
}

@Composable
private fun RadioButtonGender(
  selectedGender: String,
  onGenderSelected: (String) -> Unit,
  modifier: Modifier = Modifier
) {
  Row(modifier = modifier) {

    val genders = listOf("Nam", "Nữ")

    genders.forEach { gender ->
      Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.clickable { onGenderSelected(gender) }
      ) {
        RadioButton(
          selected = selectedGender == gender,
          onClick = { onGenderSelected(gender) }
        )
        Spacer(modifier = Modifier.width(4.dp))
        Text(text = gender)
      }
    }
  }
}

