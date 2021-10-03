package com.example.allmyfriends

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MutableLiveData
import androidx.paging.PagingData
import com.example.allmyfriends.model.Person
import com.example.allmyfriends.repository.PeopleRepository
import com.example.allmyfriends.ui.peoplelist.PeopleListViewModel
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runBlockingTest
import kotlinx.coroutines.test.setMain
import org.hamcrest.MatcherAssert.assertThat
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
class PeopleListViewModelTest {
    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    private val peopleRepository = mockk<PeopleRepository>(relaxed = true)
    private val dispatcher = TestCoroutineDispatcher()
    private lateinit var viewModel: PeopleListViewModel

    @Before
    fun setUp() {
        Dispatchers.setMain(dispatcher)
        viewModel = PeopleListViewModel(peopleRepository, dispatcher)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun testGetPeopleLocal() = runBlockingTest{
        val pagingDataExpected : PagingData<Person> = PagingData.empty()

        coEvery { peopleRepository.getPeopleLocal() } returns MutableStateFlow(pagingDataExpected)

        val pagingDataReceived = viewModel.pagingData.first()

        assert(pagingDataReceived == pagingDataExpected)
    }

    @Test
    fun testGetPeopleRemote() = runBlockingTest {
        val pagingDataExpected : PagingData<Person> = PagingData.empty()
        coEvery { peopleRepository.getPeopleRemote() } returns MutableStateFlow(pagingDataExpected)

        val pagingDataReceived = viewModel.pagingData.first()

        assert(pagingDataReceived == pagingDataExpected)
    }
}