package com.tnmd.learningenglishapp.screens.list_courses

import com.tnmd.learningenglishapp.model.service.AccountService
import com.tnmd.learningenglishapp.model.service.LogService
import com.tnmd.learningenglishapp.screens.LearningEnglishAppViewModel
import javax.inject.Inject

class CoursesViewModel @Inject constructor(
    private val accountService: AccountService,
    logService: LogService
) : LearningEnglishAppViewModel(logService){

}