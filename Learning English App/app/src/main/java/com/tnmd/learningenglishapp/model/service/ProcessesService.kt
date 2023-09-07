package com.tnmd.learningenglishapp.model.service

import com.tnmd.learningenglishapp.model.Processes
import kotlinx.coroutines.flow.Flow

interface ProcessesService {
    val processes: Flow<List<Processes>>
}