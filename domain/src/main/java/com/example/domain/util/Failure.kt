package com.example.domain.util

sealed class Failure {
    data object NetworkConnection : Failure()
    data object  UnAuthorize : Failure()
    data class ServerError(val message:String?) : Failure()
    data class UnknownError(val message:String?) : Failure()
}