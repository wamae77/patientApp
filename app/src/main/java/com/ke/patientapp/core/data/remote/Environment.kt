package com.ke.patientapp.core.data.remote

enum class Environment(
    val host: String,
    val secure: Boolean = false
) {
    //https://patientvisitapis.intellisoftkenya.com/api/
    PRODUCTION("patientvisitapis.intellisoftkenya.com", secure = true);

    private val scheme: String
        get() = if (secure) "https://" else "http://"

    private val apiPath = "api/"

    val baseUrl: String
        get() = "$scheme$host/$apiPath"

}

object ApiConfig {
    private val env = Environment.PRODUCTION
    val baseUrl: String = env.baseUrl
}
