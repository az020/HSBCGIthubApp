package com.tencent.wx.livestream.hsbcgithubapp

import android.content.SharedPreferences
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tencent.wx.livestream.hsbcgithubapp.account.AccountManager
import com.tencent.wx.livestream.hsbcgithubapp.networking.GitHubServiceManager
import com.tencent.wx.livestream.hsbcgithubapp.networking.Response
import com.tencent.wx.livestream.hsbcgithubapp.networking.UserInfo
import junit.framework.Assert.assertFalse
import junit.framework.Assert.assertTrue
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.newSingleThreadContext
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestRule
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.*
import org.mockito.MockitoAnnotations
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class AccountMangerTest  {

    lateinit var accountManager: AccountManager

    @Mock
    lateinit var sharedPreferences: SharedPreferences

    @Mock
    lateinit var gitHubService: GitHubServiceManager

    val testDispatcher = newSingleThreadContext("UI Thread")

    @get:Rule
    var rule: TestRule = InstantTaskExecutorRule()

    @Before
    fun before() {
        MockitoAnnotations.openMocks(this)
        accountManager = AccountManager
        accountManager.initialize(sharedPreferences, gitHubService)
        Dispatchers.setMain(testDispatcher)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
        testDispatcher.close()
    }

    @Test
    fun testLoginSuccess() {
        runBlocking {
            val response = Response( UserInfo(
                "testUser", "avtarUrl", "email", "today", 1, 2, 3
            ))
            val mockedSharedPrefEditor = mock(SharedPreferences.Editor::class.java)
            `when`(gitHubService.login("token")).thenReturn(response)
            `when`(sharedPreferences.edit()).thenReturn(mockedSharedPrefEditor)

            accountManager.login("token")

            verify(mockedSharedPrefEditor).putString("token", "token")
        }
    }

    @Test
    fun testLoginFail() {
        runBlocking {
            val response = Response<UserInfo>(fail = "Authentication Required")
            `when`(gitHubService.login("token")).thenReturn(response)

            accountManager.login("token")

            verifyNoInteractions(sharedPreferences)
        }
    }

    @Test
    fun testLogout() {
        val mockedSharedPrefEditor = mock(SharedPreferences.Editor::class.java)
        `when`(sharedPreferences.edit()).thenReturn(mockedSharedPrefEditor)

        accountManager.logout()

        verify(mockedSharedPrefEditor).remove("token")
    }

    @Test
    fun isLoggedInTrue() {
        `when`(sharedPreferences.getString("token", null)).thenReturn("token")

        val isLoggedIn = accountManager.isLoggedIn()

        assertTrue(isLoggedIn)
    }

    @Test
    fun isLoggedInFalse() {
        `when`(sharedPreferences.getString("token", null)).thenReturn(null)

        val isLoggedIn = accountManager.isLoggedIn()

        assertFalse(isLoggedIn)
    }

    @Test
    fun testGetUserInfo() {
        runBlocking {
            `when`(sharedPreferences.getString("token", "")).thenReturn("token")

            accountManager.getUserInfo()

            verify(gitHubService).login("token")
        }
    }
}
