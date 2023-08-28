
package com.tnmd.learningenglishapp.service.impl

import com.google.firebase.crashlytics.ktx.crashlytics
import com.google.firebase.ktx.Firebase
import com.tnmd.learningenglishapp.model.service.LogService
import javax.inject.Inject

class LogServiceImpl @Inject constructor() : LogService {
  override fun logNonFatalCrash(throwable: Throwable) =
    Firebase.crashlytics.recordException(throwable)
}
