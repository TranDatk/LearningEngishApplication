package com.tnmd.learningenglishapp.screens.list_courses_quizz

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.FloatingActionButton
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.tnmd.learningenglishapp.model.Processes
import com.tnmd.learningenglishapp.screens.list_courses.CoursesItem

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
@ExperimentalMaterialApi
fun CoursesQuizzScreen(
    openAndPopUp: (String) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: CoursesQuizzViewModel = hiltViewModel()
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = "Trang ôn tập") },
                actions = {
                },
                backgroundColor =  Color(0xFFFFC000),
                elevation = 8.dp, // Box shadow
                modifier = Modifier.clip(RoundedCornerShape( 0.dp, 0.dp, 15.dp, 15.dp))
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { /*viewModel.onUpdateClick(openScreen)*/ },
                backgroundColor = MaterialTheme.colors.primary,
                contentColor = MaterialTheme.colors.onPrimary,
                modifier = modifier.padding(16.dp)
            ) {
                Icon(Icons.Filled.Add, "Update")
            }
        }
    ) {
        val courses = viewModel.courses
        val processes = viewModel.processes

        Column(modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight()) {
            LazyColumn {
                items(courses.toList(), key = { it.id }) { coursesItem ->
                  var check = true
                    for(pro in processes){
                      if (coursesItem.id == pro.coursesId){
                          CoursesQuizzItem(
                              courses = coursesItem,
                              onCourseItemClick = openAndPopUp,
                              id = coursesItem.id,
                              process = pro,
                              viewModel = viewModel
                          )
                          check = false
                          break
                      }
                  }
                    if(check){
                        CoursesQuizzItem(
                            courses = coursesItem,
                            onCourseItemClick = openAndPopUp,
                            id = coursesItem.id,
                            process = Processes(processesCheck = 0.0),
                            viewModel = viewModel
                        )
                    }
                }
                Log.d("processes", processes.size.toString() + " " + courses.size)
            }
        }

    }


}