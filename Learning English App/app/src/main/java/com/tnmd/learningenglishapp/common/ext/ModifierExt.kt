
package com.tnmd.learningenglishapp.common.ext

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.MaterialTheme
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp

fun Modifier.textButton(): Modifier {
  return this.fillMaxWidth().padding(16.dp, 8.dp, 16.dp, 0.dp)
}

fun Modifier.basicButton(): Modifier {
  return this.fillMaxWidth().padding(16.dp, 8.dp).height(60.dp)
}

fun Modifier.card(): Modifier {
  return this.padding(16.dp, 0.dp, 16.dp, 8.dp)
}

fun Modifier.contextMenu(): Modifier {
  return this.wrapContentWidth()
}

fun Modifier.alertDialog(): Modifier {
  return this.wrapContentWidth().wrapContentHeight()
}

fun Modifier.dropdownSelector(): Modifier {
  return this.fillMaxWidth()
}

fun Modifier.fieldModifier(): Modifier {
  return this.fillMaxWidth().padding(16.dp, 4.dp)
}

fun Modifier.toolbarActions(): Modifier {
  return this.wrapContentSize(Alignment.TopEnd)
}

fun Modifier.spacer(): Modifier {
  return this.fillMaxWidth().padding(15.dp)
}

fun Modifier.smallSpacer(): Modifier {
  return this.fillMaxWidth().height(8.dp)
}

/*--------------------------------------
This is for screen_courses
*/

fun Modifier.cardCourses(): Modifier {
  return this.padding(8.dp)
    .fillMaxWidth()
    .height(125.dp)
}

fun Modifier.rowCardCourses(): Modifier {
  return this.fillMaxSize()
    .padding(16.dp)
}

fun Modifier.AsyncImageCardCourses(): Modifier {
  return this.size(50.dp)
    .clip(CircleShape)
}
/*-----------------------------------*/