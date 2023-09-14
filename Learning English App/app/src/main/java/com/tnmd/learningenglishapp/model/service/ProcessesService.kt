package com.tnmd.learningenglishapp.model.service

import com.tnmd.learningenglishapp.model.Processes
import kotlinx.coroutines.flow.Flow

interface ProcessesService {
    val processes: Flow<List<Processes>>
    suspend fun newAndUpdateProcesses(processes: Processes)
    suspend fun getProcessesByCoursesId(coursesId : String) : Processes?
    suspend fun updateProcessesLearn(processes: Processes)
}