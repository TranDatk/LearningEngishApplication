package com.tnmd.learningenglishapp.model.service

import com.tnmd.learningenglishapp.model.Learner
import kotlinx.coroutines.flow.Flow

interface LearnerService {
    val learner: Flow<Learner?>?
}