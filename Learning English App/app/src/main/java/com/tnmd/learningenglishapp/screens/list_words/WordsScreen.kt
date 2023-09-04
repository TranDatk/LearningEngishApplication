package com.tnmd.learningenglishapp.screens.list_words

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import com.tnmd.learningenglishapp.screens.login.LoginViewModel

@Composable
fun WordsScreen(
    openScreen: (String) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: WordsViewModel = hiltViewModel()
) {

}