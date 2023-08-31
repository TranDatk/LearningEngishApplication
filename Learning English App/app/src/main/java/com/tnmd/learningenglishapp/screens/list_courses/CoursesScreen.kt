package com.tnmd.learningenglishapp.screens.list_courses

import android.annotation.SuppressLint
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
@ExperimentalMaterialApi
fun CoursesScreen(
    openScreen: (String) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: CoursesViewModel = hiltViewModel()
){

}