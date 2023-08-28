
package com.tnmd.learningenglishapp.screens

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tnmd.learningenglishapp.common.snackbar.SnackbarManager
import com.tnmd.learningenglishapp.common.snackbar.SnackbarMessage.Companion.toSnackbarMessage
import com.tnmd.learningenglishapp.model.service.LogService
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

open class LearningEnglishAppViewModel(private val logService: LogService) : ViewModel() {
  fun launchCatching(snackbar: Boolean = true, block: suspend CoroutineScope.() -> Unit) =
    viewModelScope.launch(
      CoroutineExceptionHandler { _, throwable ->
        if (snackbar) {
          SnackbarManager.showMessage(throwable.toSnackbarMessage())
        }
        logService.logNonFatalCrash(throwable)
      },
      block = block
    )
}
