

package com.tnmd.learningenglishapp.screens.settings

import android.content.Intent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.tnmd.learningenglishapp.activity.MainActivity
import com.tnmd.learningenglishapp.common.composable.BasicToolbar
import com.tnmd.learningenglishapp.common.ext.spacer
import com.tnmd.learningenglishapp.composable.RegularCardEditor
import com.tnmd.learningenglishapp.R.drawable as AppIcon
import com.tnmd.learningenglishapp.R.string as AppText

@ExperimentalMaterialApi
@Composable
fun SettingsScreen(
  restartApp: (String) -> Unit,
  openScreen: (String) -> Unit,
  modifier: Modifier = Modifier,
  viewModel: SettingsViewModel = hiltViewModel()
) {
  val uiState by viewModel.uiState

  Column(
    modifier = modifier
      .fillMaxWidth()
      .fillMaxHeight()
      .verticalScroll(rememberScrollState()),
    horizontalAlignment = Alignment.CenterHorizontally,
  ) {
    BasicToolbar(AppText.settings)

    Spacer(modifier = Modifier.spacer())

    Column(
      modifier = modifier
        .fillMaxWidth()
        .fillMaxHeight()
   .padding(20.dp),
      horizontalAlignment = Alignment.CenterHorizontally,
    )  {
      if (!uiState.isAnonymousAccount) {
        RegularCardEditor(AppText.sign_in, AppIcon.ic_sign_in, "", Modifier.padding(20.dp)) {
          viewModel.onLoginClick(openScreen)
        }
        Spacer(modifier = Modifier.spacer())
        RegularCardEditor(AppText.create_account, AppIcon.ic_create_account, "", Modifier.padding(20.dp)) {
          viewModel.onSignUpClick(openScreen)
        }
      } else {
        val intent = Intent(LocalContext.current, MainActivity::class.java)
        LocalContext.current.startActivity(intent)
      }
    }

  }
}


