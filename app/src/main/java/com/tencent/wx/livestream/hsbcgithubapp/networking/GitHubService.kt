package com.tencent.wx.livestream.hsbcgithubapp.networking

import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query

/**
 * GitHub REST API interface (https://api.github.com/)
 */
interface GitHubService  {

    @GET("search/repositories")
    suspend fun search(
        @Query("q") query: String
    ) : RepoResponse

    @GET("user")
    suspend fun login(
        @Header("Authorization") token: String
    ): UserInfo
}
