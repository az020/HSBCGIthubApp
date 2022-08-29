package com.tencent.wx.livestream.hsbcgithubapp.account

import android.content.SharedPreferences
import com.tencent.wx.livestream.hsbcgithubapp.networking.GitHubServiceManager
import com.tencent.wx.livestream.hsbcgithubapp.networking.Response
import com.tencent.wx.livestream.hsbcgithubapp.networking.UserInfo

/**
 * Manages user's login, logout activities, saves account info
 */
object AccountManager {

    private lateinit var sharedPrefs: SharedPreferences
    private lateinit var gitHubService: GitHubServiceManager

    fun initialize(sharedPrefs: SharedPreferences, gitHubService: GitHubServiceManager) {
        this.sharedPrefs = sharedPrefs
        this.gitHubService = gitHubService
    }

    suspend fun getUserInfo(): Response<UserInfo> {
        val token = sharedPrefs.getString("token", "") ?: ""
        val response = gitHubService.login(token)
        return response
    }

    suspend fun login(token: String): Response<UserInfo> {
        val response = gitHubService.login(token)
        response.success?.let {
            with(sharedPrefs.edit()) {
                putString("token", token)
                apply()
            }
        }
        return response
    }

    fun isLoggedIn(): Boolean {
        val token = sharedPrefs.getString("token", null)
        return token != null
    }

    fun logout() {
        with(sharedPrefs.edit()) {
            remove("token")
            apply()
        }
    }

//    private fun getEncryptedSharedPreference(context: Context): SharedPreferences {
//        val sharedPrefsFile = "HSBCSharedPrefs"
//        val keyGenParameterSpec = MasterKeys.AES256_GCM_SPEC
//        val mainKeyAlias = MasterKeys.getOrCreate(keyGenParameterSpec)
//
//        return EncryptedSharedPreferences.create(
//            sharedPrefsFile,
//            mainKeyAlias,
//            context.applicationContext,
//            EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
//            EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
//        )
//    }



}
