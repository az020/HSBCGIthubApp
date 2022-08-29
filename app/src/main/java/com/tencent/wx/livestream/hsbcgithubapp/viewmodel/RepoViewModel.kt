package com.tencent.wx.livestream.hsbcgithubapp.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.tencent.wx.livestream.hsbcgithubapp.networking.GitHubService
import com.tencent.wx.livestream.hsbcgithubapp.networking.GitHubServiceManager
import com.tencent.wx.livestream.hsbcgithubapp.networking.RepoItem
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class RepoViewModel(private val gitHubService: GitHubServiceManager) : ViewModel() {

    val repoList = MutableLiveData<List<RepoItem>>()
    val total = MutableLiveData<Int>()
    val errMsg = MutableLiveData<String>()

    fun search(query: String) {
        CoroutineScope((Dispatchers.IO)).launch {
            val response = gitHubService.search(query)

            withContext(Dispatchers.Main) {
                response.success?.let { repoResponse ->
                    repoList.postValue(repoResponse.items)
                    total.postValue(repoResponse.total)
                }
                response.fail?.let { err ->
                    errMsg.postValue(err)
                }
            }
        }
    }

    class Factory(private val gitHubService: GitHubServiceManager) : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return RepoViewModel(gitHubService) as T
        }
    }

}

