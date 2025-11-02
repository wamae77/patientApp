package com.ke.patientapp.core.sync

import androidx.work.BackoffPolicy
import androidx.work.Constraints
import androidx.work.NetworkType

val netConstraints = Constraints.Builder()
    .setRequiredNetworkType(NetworkType.CONNECTED)
    .build()

val backoff = BackoffPolicy.EXPONENTIAL




