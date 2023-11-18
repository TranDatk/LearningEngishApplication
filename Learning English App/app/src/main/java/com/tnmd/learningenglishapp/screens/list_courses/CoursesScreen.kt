package com.tnmd.learningenglishapp.screens.list_courses

import android.annotation.SuppressLint
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
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
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.Role.Companion.Image
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.dataObjects
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.tnmd.learningenglishapp.R
import com.tnmd.learningenglishapp.model.Courses
import com.tnmd.learningenglishapp.model.Processes
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
        topBar = {
            TopAppBar(
                title = { Text(text = "Trang chủ") },
                navigationIcon = {
                    IconButton(onClick = { /* Handle navigation icon click */ }) {
                        Icon(imageVector = Icons.Default.Menu, contentDescription = null)
                    }
                },
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
        val imageRes = painterResource(id = R.drawable.img_star)

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight()
                .padding(10.dp)
                .verticalScroll(rememberScrollState())
        ) {
            Row(
                modifier = Modifier
                    .background(color = Color(0xFFFCFAF2))
                    .border(0.dp, color = Color.Gray, shape = RoundedCornerShape(12.dp))
                    .fillMaxWidth()
                    .height(60.dp),
                Arrangement.Center,
                Alignment.CenterVertically
            ) {
                Image(
                    painter = imageRes,
                    contentDescription = null,
                    modifier = Modifier
                        .height(25.dp)
                        .clip(RoundedCornerShape(12.dp)),
                    contentScale = ContentScale.Crop
                )
                Text(
                    text = "Giảm giá 50% cho tài khoản Premium",
                    style = MaterialTheme.typography.subtitle2,
                    color = Color.Black,
                    modifier = Modifier
                        .padding(8.dp)
                        .clip(RoundedCornerShape(12.dp))
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            // LazyColumn
            LazyColumn(
                modifier = Modifier.weight(1f)
            ) {
                items(courses.toList(), key = { it.id }) { coursesItem ->
                    var check = true
                    for (pro in processes) {
                        if (coursesItem.id == pro.coursesId) {
                            CoursesItem(
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
                    if (check) {
                        CoursesItem(
                            courses = coursesItem,
                            onCourseItemClick = openAndPopUp,
                            id = coursesItem.id,
                            process = Processes(processesLearn = 0.0),
                            viewModel = viewModel
                        )
                    }
                }
            }
        }


    }


}