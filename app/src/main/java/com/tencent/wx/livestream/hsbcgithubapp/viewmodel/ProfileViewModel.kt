package com.tencent.wx.livestream.hsbcgithubapp.viewmodel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.tencent.wx.livestream.hsbcgithubapp.account.AccountManager
import com.tencent.wx.livestream.hsbcgithubapp.networking.GitHubServiceManager
import com.tencent.wx.livestream.hsbcgithubapp.networking.UserInfo
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ProfileViewModel(private val accountManager: AccountManager): ViewModel() {

    val userInfo = MutableLiveData<UserInfo>()
    val errMsg = MutableLiveData<String>()

    fun getUserInfo() {
        CoroutineScope((Dispatchers.IO)).launch {
            withContext(Dispatchers.Main) {
                val response = accountManager.getUserInfo()
                withContext(Dispatchers.Main) {}
                response?.success?.let {
//                    Log.i("HSBC ProfileVM", "login resp: name = ${it.login}")
                    userInfo.postValue(it)
                }
                response.fail?.let { err ->
                    errMsg.postValue(err)
                }
            }
        }
    }

    class Factory(private val accountManager: AccountManager) : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return ProfileViewModel(accountManager) as T
        }
    }
}
