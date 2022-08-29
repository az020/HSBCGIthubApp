package com.tencent.wx.livestream.hsbcgithubapp

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tencent.wx.livestream.hsbcgithubapp.networking.GitHubServiceManager
import com.tencent.wx.livestream.hsbcgithubapp.networking.RepoItem
import com.tencent.wx.livestream.hsbcgithubapp.networking.RepoResponse
import com.tencent.wx.livestream.hsbcgithubapp.networking.Response
import com.tencent.wx.livestream.hsbcgithubapp.viewmodel.RepoViewModel
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


/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
@RunWith(MockitoJUnitRunner::class)
class RepoViewModelTest {

    @Mock
    lateinit var gitHubServiceManger: GitHubServiceManager

    lateinit var repoViewModel: RepoViewModel

    val testDispatcher = newSingleThreadContext("UI Thread")

    @get:Rule
    var rule: TestRule = InstantTaskExecutorRule()

    @Before
    fun before() {
        MockitoAnnotations.openMocks(this)
        repoViewModel = RepoViewModel(gitHubServiceManger)
        Dispatchers.setMain(testDispatcher)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
        testDispatcher.close()
    }

    @Test
    fun testSearchSuccess() {

         runBlocking {
             val repoListExpected = listOf(
                RepoItem("android1", "android description 1", 3, "20220101"),
                RepoItem("android2", "android description 2", 15, "20220102")
             )
             val response = Response(
             RepoResponse(134, repoListExpected))
             Mockito.`when`(gitHubServiceManger.search("android")).thenReturn(response)

             repoViewModel.search("android")
             delay(500)
             val repoList = repoViewModel.repoList.value
             assertNotNull(repoList)
             assertEquals(repoList!!.size, repoListExpected.size)
             assertEquals(repoList[0], repoListExpected[0])
             assertEquals(repoList[1], repoListExpected[1])

             val errMsg = repoViewModel.errMsg.value
             assertNull(errMsg)
        }
    }

    @Test
    fun testSearchFail() {
        runBlocking {
            val response = Response<RepoResponse>(fail = "Authentication Required")
            Mockito.`when`(gitHubServiceManger.search("android")).thenReturn(response)

            repoViewModel.search("android")
            delay(500)

            val repoList = repoViewModel.repoList.value
            assertNull(repoList)

            val errMsg = repoViewModel.errMsg.value
            assertEquals("Authentication Required", errMsg)
        }
    }

}
