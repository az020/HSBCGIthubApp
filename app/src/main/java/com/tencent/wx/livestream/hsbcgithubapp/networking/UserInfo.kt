package com.tencent.wx.livestream.hsbcgithubapp.networking

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass


@JsonClass(generateAdapter = true)
data class UserInfo (

    val login: String? = null,

    @Json(name = "avatar_url")
    val avatar: String? = null,

    val email: String? = null,

    @Json(name="created_at")
    val joinedAt: String? = null,

    @Json(name="public_repos")
    val publicRepos: Int = 0,

    val followers: Int = 0,

    val following: Int = 0
)
