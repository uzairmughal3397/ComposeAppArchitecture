package com.uzair.composeBase.data.genericModelClasses

data class ResponseError(
    val message: String?,
    val code: Int?,
    val accessToken: String?
)
