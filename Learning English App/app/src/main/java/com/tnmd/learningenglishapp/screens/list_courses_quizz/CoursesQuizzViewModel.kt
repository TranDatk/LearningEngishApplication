package com.tnmd.learningenglishapp.screens.list_courses_quizz

import android.util.Log
import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.SavedStateHandle
import com.tnmd.learningenglishapp.LIST_WORDS
import com.tnmd.learningenglishapp.COURSES_ID
import com.tnmd.learningenglishapp.LIST_REVIEW_SCREEN
import com.tnmd.learningenglishapp.common.snackbar.SnackbarManager
import com.tnmd.learningenglishapp.model.Courses
import com.tnmd.learningenglishapp.model.Processes
import com.tnmd.learningenglishapp.model.service.CoursesService
import com.tnmd.learningenglishapp.model.service.LogService
import com.tnmd.learningenglishapp.model.service.ProcessesService
import com.tnmd.learningenglishapp.screens.LearningEnglishAppViewModel
import com.tnmd.learningenglishapp.screens.list_review.ReviewUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject
import com.tnmd.learningenglishapp.R.string as AppText

@HiltViewModel
class CoursesQuizzViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    private val coursesService: CoursesService,
    private val processesService: ProcessesService,
    logService: LogService
) : LearningEnglishAppViewModel(logService){

    val courses = mutableStateListOf<Courses>()
    val processes = mutableStateListOf<Processes>()
    private val _uiState = MutableStateFlow(List_Courses_QuizzUiState())
    val uiState: StateFlow<List_Courses_QuizzUiState> = _uiState.asStateFlow()

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
        checkCompleteCoursesYet(id) { result ->
            if(result == true){
                openAndPopUp("$LIST_REVIEW_SCREEN?$COURSES_ID={${id}}")
            }else{
                SnackbarManager.showMessage(AppText.NotCompleteCourses)
            }
        }

    }

    fun checkCompleteCoursesYet(id: String, callback: (Boolean) -> Unit) {
        val epsilon = 0.001 // Đặt một khoảng sai số nhỏ
        launchCatching {
            val processes = processesService.getProcessesByCoursesId(id)
            Log.d("processesService.getProcessesByCoursesId", processes.toString())
            val result = processes?.processesLearn != null && Math.abs(processes.processesLearn - 1.0) < epsilon
            callback(result)
        }
    }
}