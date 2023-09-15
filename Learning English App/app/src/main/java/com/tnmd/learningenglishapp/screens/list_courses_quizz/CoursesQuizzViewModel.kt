package com.tnmd.learningenglishapp.screens.list_courses_quizz

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.SavedStateHandle
import com.tnmd.learningenglishapp.LIST_WORDS
import com.tnmd.learningenglishapp.COURSES_ID
import com.tnmd.learningenglishapp.LIST_REVIEW_SCREEN
import com.tnmd.learningenglishapp.model.Courses
import com.tnmd.learningenglishapp.model.Processes
import com.tnmd.learningenglishapp.model.service.CoursesService
import com.tnmd.learningenglishapp.model.service.LogService
import com.tnmd.learningenglishapp.model.service.ProcessesService
import com.tnmd.learningenglishapp.screens.LearningEnglishAppViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class CoursesQuizzViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    private val coursesService: CoursesService,
    private val processesService: ProcessesService,
    logService: LogService
) : LearningEnglishAppViewModel(logService){
    val courses = mutableStateListOf<Courses>()
    val processes = mutableStateListOf<Processes>()

    init {
            launchCatching {
                coursesService.courses.collect { courseList ->
                    courses.clear() // Xóa danh sách hiện tại (nếu cần)
                    courses.addAll(courseList) // Thêm dữ liệu mới vào danh sách
                }
            }
        launchCatching {
            processesService.processes.collect{processList ->
                processes.clear()
                processes.addAll(processList)
            }
        }
    }


    fun onCourseItemClick(openAndPopUp: (String) -> Unit, id : String) {
        openAndPopUp("$LIST_REVIEW_SCREEN?$COURSES_ID={${id}}")
    }
}