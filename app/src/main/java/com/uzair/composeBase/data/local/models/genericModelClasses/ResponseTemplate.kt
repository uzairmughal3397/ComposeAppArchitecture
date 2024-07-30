package com.uzair.composeBase.data.local.models.genericModelClasses

class ResponseTemplate<T>(
    val accessToken: String?,
    val code: Int?,
    val `data`: T?,
    val message: String?,
)

