package com.tnmd.learningenglishapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.ExperimentalMaterialApi

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.tnmd.learningenglishapp.ui.theme.LearningEnglishAppTheme
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
@ExperimentalMaterialApi
class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            LearningEnglishApp()
        }
    }
}

