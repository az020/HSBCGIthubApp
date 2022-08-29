package com.tencent.wx.livestream.hsbcgithubapp.networking

import android.util.Log
import okhttp3.OkHttpClient
import retrofit2.HttpException
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import java.io.IOException
import java.net.SocketTimeoutException

const val BASE_URL = "https://api.github.com/"

/**
 * Implementation of [GitHubService]
 */
object GitHubServiceManager {

    private val gitHubService: GitHubService

    init {
        val okHttpClient = OkHttpClient.Builder()
//            .addInterceptor(LoggingInterceptor())
            .build()

        val retrofit = Retrofit.Builder()
            .addConverterFactory(MoshiConverterFactory.create())
            .client(okHttpClient)
            .baseUrl(BASE_URL)
            .build()

        gitHubService = retrofit.create(GitHubService::class.java)
    }

    suspend fun search(keyword: String): Response<RepoResponse> {
        return try {
            val searchResponse = gitHubService.search(keyword)
            Log.i("HSBC GHServiceManager", "searched result: ${searchResponse.total} for $keyword")
            return Response(success = searchResponse)
        } catch (exception: HttpException) {
            Response(fail = exception.message)
        } catch (exception: SocketTimeoutException) {
            Response(fail = exception.message)
        } catch (exception: IOException) {
            Response(fail = exception.message)
        }
    }

    suspend fun login(token: String): Response<UserInfo> {
        return try {
            val user = gitHubService.login("token " + token)
            Log.i("HSBC GHServiceManager", "logged in username: ${user.login}")
            return Response(success = user)
        } catch (exception: HttpException) {
            Response(fail = exception.message)
        } catch (exception: SocketTimeoutException) {
            Response(fail = exception.message)
        } catch (exception: IOException) {
            Response(fail = exception.message)
        }
    }
}
