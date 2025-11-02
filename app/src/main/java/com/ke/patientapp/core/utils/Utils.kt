package com.ke.patientapp.core.utils

import android.content.Context
import android.util.Base64
import com.ke.patientapp.R
import java.security.MessageDigest
import java.security.cert.CertificateFactory
import java.security.cert.X509Certificate

fun Context.getPinnedCertHash(): String {
    val inputStream = this.resources.openRawResource(R.raw.intellisoftkenya)
    val certFactory = CertificateFactory.getInstance("X.509")
    val cert = certFactory.generateCertificate(inputStream) as X509Certificate
    val publicKey = cert.publicKey.encoded
    val sha256 = MessageDigest.getInstance("SHA-256").digest(publicKey)
    return "sha256/${Base64.encodeToString(sha256, Base64.NO_WRAP)}"
}
