package com.tnmd.learningenglishapp.screens.list_courses_quizz

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
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
import androidx.compose.material.Card
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.LinearProgressIndicator
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import com.tnmd.learningenglishapp.common.ext.AsyncImageCardCourses
import com.tnmd.learningenglishapp.common.ext.cardCourses
import com.tnmd.learningenglishapp.model.Courses
import com.tnmd.learningenglishapp.model.Processes

@Composable
@ExperimentalMaterialApi
fun CoursesQuizzItem(
    courses: Courses,
    process: Processes,
    onCourseItemClick: (String) -> Unit,
    id : String,
    viewModel: CoursesQuizzViewModel = hiltViewModel()
) {
    Card(
        modifier = Modifier
            .cardCourses()
            .clickable { viewModel.onCourseItemClick(onCourseItemClick, id) },
        elevation = 4.dp
    ) {
        Box(
            modifier = Modifier.fillMaxSize()
        ) {
            Column(
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxSize()
                    .align(Alignment.TopStart) // Đặt cách trên cùng
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    AsyncImage(
                        model = courses.image,
                        contentDescription = null,
                        modifier = Modifier
                            .AsyncImageCardCourses()
                            .background(MaterialTheme.colors.onPrimary)
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
                        Row(
                            modifier = Modifier
                                .align(Alignment.CenterHorizontally)
                                .padding(top = 10.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            LinearProgressIndicator(
                                progress = (process.processesCheck.toFloat()),
                                modifier = Modifier.weight(1f)
                            )
                            Text(
                                text = "${(process.processesCheck * 100).toInt()}%",
                                color = Color.Black,
                                style = MaterialTheme.typography.caption,
                                modifier = Modifier.alignByBaseline()
                            )
                        }

                    }
                    IconButton(
                        onClick = { viewModel.onCourseItemClick(onCourseItemClick, id) },
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
            Spacer(modifier = Modifier.height(8.dp))
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.BottomCenter) // Đặt ở dưới cùng
            ) {

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