package com.tencent.wx.livestream.hsbcgithubapp.networking

/**
 * Common response interface for async tasks
 */
data class Response<T> (
    val success: T? = null,

    val fail: String? = null
)
