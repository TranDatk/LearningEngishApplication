package com.tnmd.learningenglishapp.screens.list_courses

import android.annotation.SuppressLint
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.FloatingActionButton
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.dataObjects
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.tnmd.learningenglishapp.model.Courses
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.runBlocking

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
@ExperimentalMaterialApi
fun CoursesScreen(
    openAndPopUp: (String) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: CoursesViewModel = hiltViewModel()
) {
    Scaffold(
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
                  for(pro in processes){
                      if (coursesItem.id == pro.coursesId){
                          CoursesItem(
                              courses = coursesItem,
                              onCourseItemClick = openAndPopUp,
                              id = coursesItem.id,
                              process = pro,
                              viewModel = viewModel
                          )
                          break
                      }
                  }
                }
                Log.d("processes", processes.size.toString() + " " + courses.size)
            }
        }

    }


}