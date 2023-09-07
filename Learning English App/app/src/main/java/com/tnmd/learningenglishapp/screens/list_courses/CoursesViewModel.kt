package com.tnmd.learningenglishapp.screens.list_courses

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.tnmd.learningenglishapp.LIST_WORDS
import com.tnmd.learningenglishapp.COURSES_ID
import com.tnmd.learningenglishapp.model.Courses
import com.tnmd.learningenglishapp.model.service.CoursesService
import com.tnmd.learningenglishapp.model.service.LogService
import com.tnmd.learningenglishapp.screens.LearningEnglishAppViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CoursesViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    private val coursesService: CoursesService,
    logService: LogService
) : LearningEnglishAppViewModel(logService){
    val courses = mutableStateListOf<Courses>()

    init {
            viewModelScope.launch {
                coursesService.courses.collect { courseList ->
                    courses.clear() // Xóa danh sách hiện tại (nếu cần)
                    courses.addAll(courseList) // Thêm dữ liệu mới vào danh sách
                }
            }
    }


    fun onCourseItemClick(openAndPopUp: (String) -> Unit, id : String) {
        openAndPopUp("$LIST_WORDS?$COURSES_ID={${id}}")
    }
}