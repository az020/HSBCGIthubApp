package com.tencent.wx.livestream.hsbcgithubapp
import android.content.Context
import android.content.SharedPreferences
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tencent.wx.livestream.hsbcgithubapp.account.AccountManager
import com.tencent.wx.livestream.hsbcgithubapp.networking.*
import com.tencent.wx.livestream.hsbcgithubapp.viewmodel.ProfileViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.newSingleThreadContext
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestRule
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.MockitoAnnotations
import org.mockito.junit.MockitoJUnitRunner


@RunWith(MockitoJUnitRunner::class)
class ProfileViewModelTest {

    @Mock
    lateinit var accountManager: AccountManager

    @Mock
    lateinit var sharedPreferences: SharedPreferences

    @Mock
    lateinit var gitHubService: GitHubServiceManager

    lateinit var profileViewModel: ProfileViewModel

    val testDispatcher = newSingleThreadContext("UI Thread")

    @get:Rule
    var rule: TestRule = InstantTaskExecutorRule()

    @Before
    fun before() {
        MockitoAnnotations.openMocks(this)
        accountManager.initialize(sharedPreferences, gitHubService)
        profileViewModel = ProfileViewModel(accountManager)
        Dispatchers.setMain(testDispatcher)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
        testDispatcher.close()
    }

    @Test
    fun testGetUserInfoSuccess() {

        runBlocking {
            val expectedUserInfo = UserInfo(
                "testUser", "avtarUrl", "email", "today", 1, 2, 3
            )
            val response = Response(expectedUserInfo)
            Mockito.`when`(accountManager.getUserInfo()).thenReturn(response)

            profileViewModel.getUserInfo()
            delay(500)

            val userInfo = profileViewModel.userInfo.value
            assertNotNull(userInfo)
            assertEquals(expectedUserInfo, userInfo)

            val errMsg = profileViewModel.errMsg.value
            assertNull(errMsg)
        }
    }

    @Test
    fun testSearchFail() {
        runBlocking {
            val response = Response<UserInfo>(fail = "Authentication Required")
            Mockito.`when`(accountManager.getUserInfo()).thenReturn(response)

            profileViewModel.getUserInfo()
            delay(500)

            val userInfo = profileViewModel.userInfo.value
            assertNull(userInfo)

            val errMsg = profileViewModel.errMsg.value
            assertEquals("Authentication Required", errMsg)
        }
    }

}
