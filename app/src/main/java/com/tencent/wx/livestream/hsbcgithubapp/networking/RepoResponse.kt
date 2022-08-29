package com.tencent.wx.livestream.hsbcgithubapp.networking

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class RepoResponse (

    @Json(name = "total_count")
    val total: Int = 0,
    @Json(name = "items")
    val items: List<RepoItem>
)

@JsonClass(generateAdapter = true)
data class RepoItem (
    @Json(name = "full_name")
    val fullName: String? = null,

    val description: String? = null,

    @Json(name = "stargazers_count")
    val stargazers: Int = 0,

    @Json(name="updated_at")
    val updateTime: String? = null
)
