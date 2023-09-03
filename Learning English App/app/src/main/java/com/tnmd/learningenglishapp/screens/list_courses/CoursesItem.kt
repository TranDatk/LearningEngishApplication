package com.tnmd.learningenglishapp.screens.list_courses

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Card
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.compose.rememberImagePainter
import com.tnmd.learningenglishapp.R
import com.tnmd.learningenglishapp.common.ext.AsyncImageCardCourses
import com.tnmd.learningenglishapp.common.ext.cardCourses
import com.tnmd.learningenglishapp.common.ext.rowCardCourses
import com.tnmd.learningenglishapp.model.Courses

@Composable
@ExperimentalMaterialApi
fun CoursesItem(
    courses: Courses,
    onCourseItemClick: (String) -> Unit
) {
    Card(
        modifier = Modifier.cardCourses().clickable { onCourseItemClick("") },
        elevation = 4.dp
    ) {
        Row(
            modifier = Modifier.rowCardCourses(),
            verticalAlignment = Alignment.CenterVertically
        ) {

            AsyncImage(
                model = courses.image,
                contentDescription = null,
                modifier = Modifier.AsyncImageCardCourses().background(MaterialTheme.colors.onPrimary)
            )
            Spacer(modifier = Modifier.width(16.dp))
            Column(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight()
            ) {
                Text(
                    text = courses.nameCourses,
                    color = Color.DarkGray,
                    style = MaterialTheme.typography.h6
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = courses.description,
                    color = Color.Gray,
                    style = MaterialTheme.typography.body2
                )
            }
            IconButton(
                onClick = { /* Xử lý sự kiện khi người dùng bấm nút */ },
                modifier = Modifier.size(24.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.ArrowForward,
                    contentDescription = null,
                    tint = Color.Gray
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Preview
@Composable
fun ComposablePreview() {
    /*CoursesItem(courses = Courses(description = "Mo ta",nameCourses = "khoa hocj cap toc"))*/
}