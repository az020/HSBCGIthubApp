package com.tencent.wx.livestream.hsbcgithubapp

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKeys
import com.tencent.wx.livestream.hsbcgithubapp.account.AccountManager
import com.tencent.wx.livestream.hsbcgithubapp.networking.GitHubServiceManager

class HSBCGitHubApplication: Application() {

    override fun onCreate() {
        super.onCreate()
        AccountManager.initialize(
            getEncryptedSharedPreference(this.applicationContext),
            GitHubServiceManager
        )
    }

    private fun getEncryptedSharedPreference(context: Context): SharedPreferences {
        val sharedPrefsFile = "HSBCSharedPrefs"
        val keyGenParameterSpec = MasterKeys.AES256_GCM_SPEC
        val mainKeyAlias = MasterKeys.getOrCreate(keyGenParameterSpec)

        return EncryptedSharedPreferences.create(
            sharedPrefsFile,
            mainKeyAlias,
            context.applicationContext,
            EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
            EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
        )
    }
}
