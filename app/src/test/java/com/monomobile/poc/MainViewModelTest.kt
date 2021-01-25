package com.monomobile.poc

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.monomobile.poc.api.ApiRepository
import com.monomobile.poc.api.Resource
import com.monomobile.poc.model.ArtistItem
import com.monomobile.poc.ui.main.MainViewModel
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.timeout
import com.nhaarman.mockitokotlin2.verify
import com.nhaarman.mockitokotlin2.whenever
import kotlinx.coroutines.*
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.*
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

@RunWith(JUnit4::class)
class MainViewModelTest {
    private lateinit var viewModel: MainViewModel
    private val apiRepository: ApiRepository = mock()
    private val apiObserver: Observer<Resource<List<ArtistItem>>> = mock()
    private val artistItemList = listOf(
        ArtistItem(
            appearance = listOf("1"),
            name = "Adam Pinkman"
        )
    )
    private val successResource = Resource.success(artistItemList)
    private val errorResource = Resource.error("Unauthorised", null)

    @Rule
    @JvmField
    val instantExecutorRule = InstantTaskExecutorRule()

    @ObsoleteCoroutinesApi
    private val mainThreadSurrogate = newSingleThreadContext("UI thread")

    @ExperimentalCoroutinesApi
    @ObsoleteCoroutinesApi
    @Before
    fun setUp() {
        Dispatchers.setMain(mainThreadSurrogate)
        viewModel = MainViewModel(apiRepository)
    }

    @ObsoleteCoroutinesApi
    @ExperimentalCoroutinesApi
    @After
    fun tearDown() {
        Dispatchers.resetMain() // reset main dispatcher to the original Main dispatcher
        mainThreadSurrogate.close()
    }

    @ExperimentalCoroutinesApi
    @Test
    fun `when getSearchResults called, then observer is updated with success`()  = runBlocking {
        whenever(apiRepository.getSearchResults()).thenReturn(successResource)
        viewModel.artists.observeForever(apiObserver)
        viewModel.getSearchResults()
        delay(10)
        verify(apiRepository).getSearchResults()
        verify(apiObserver, timeout(50)).onChanged(Resource.loading("init"))
        verify(apiObserver, timeout(50)).onChanged(successResource)
    }

    @Test
    fun `when getSearchResults is called with error, then observer is updated with failure`() = runBlocking {
        whenever(apiRepository.getSearchResults()).thenReturn(errorResource)
        viewModel.artists.observeForever(apiObserver)
        viewModel.getSearchResults()
        delay(10)
        verify(apiRepository).getSearchResults()
        verify(apiObserver, timeout(50)).onChanged(Resource.loading("init"))
        verify(apiObserver, timeout(50)).onChanged(errorResource)
    }
}