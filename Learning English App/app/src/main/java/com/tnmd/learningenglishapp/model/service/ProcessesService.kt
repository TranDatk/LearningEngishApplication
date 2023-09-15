package com.tnmd.learningenglishapp.model.service

import com.tnmd.learningenglishapp.model.Processes
import kotlinx.coroutines.flow.Flow

interface ProcessesService {
    val processes: Flow<List<Processes>>
    suspend fun newAndUpdateProcessesByLearn(processes: Processes)
    suspend fun getProcessesByCoursesId(coursesId : String) : Processes?
    suspend fun updateProcessesLearn(processes: Processes)
    suspend fun newAndUpdateProcessesByReview(processes: Processes)
}