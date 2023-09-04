package com.tnmd.learningenglishapp.screens.list_words;

import com.tnmd.learningenglishapp.model.service.AuthenticationService;
import com.tnmd.learningenglishapp.model.service.LogService;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;

@HiltViewModel
class WordsViewModel @Inject constructor(
        logService: LogService,
        private val authenticationService: AuthenticationService
) : LearningEnglishAppViewModel(logService) {

}


