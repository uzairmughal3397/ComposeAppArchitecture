package com.uzair.composeBase.data.local.models.genericModelClasses

data class ResponseError(
    val message: String?,
    val code: Int?,
    val accessToken: String?
)
