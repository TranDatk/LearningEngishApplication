package com.tnmd.learningenglishapp.common.composable

import androidx.annotation.StringRes
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

@Composable
fun BasicToolbar(@StringRes title: Int) {
  val darkMode = isSystemInDarkTheme()
  val toolbarColor = if (darkMode) Color(0xFFE8FFCE) else Color(0xFFFFC000)

  TopAppBar(
    title = {
      Text(
        text = stringResource(title),
        color = Color(0xFF262626),
        style = MaterialTheme.typography.h6.copy(fontWeight = FontWeight.W600),
      )
    },
    backgroundColor = Color.Transparent,
    elevation = 0.dp,
    modifier = Modifier
      .height(70.dp)
      .fillMaxWidth()
      .background(toolbarColor)
      .border(
        width = 0.dp,
        color = Color.Transparent,
        shape = RoundedCornerShape(
          bottomEnd = 15.dp,
          bottomStart = 15.dp
        )
      )
  )

}


