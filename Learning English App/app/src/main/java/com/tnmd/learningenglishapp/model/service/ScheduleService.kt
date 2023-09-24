package com.tnmd.learningenglishapp.model.service

import com.tnmd.learningenglishapp.model.Processes
import com.tnmd.learningenglishapp.model.Schedule
import kotlinx.coroutines.flow.Flow

interface ScheduleService {
    val schedule: Flow<List<Schedule>>
    suspend fun newSchedule(schedule: Schedule) : String
    suspend fun removeSchedule(schedule: Schedule)
}