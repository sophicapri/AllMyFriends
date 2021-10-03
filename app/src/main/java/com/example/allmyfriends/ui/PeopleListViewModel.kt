package com.example.allmyfriends.ui

import androidx.lifecycle.*
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.allmyfriends.model.Person
import com.example.allmyfriends.repository.PeopleRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.*
import javax.inject.Inject

import kotlinx.coroutines.launch


@HiltViewModel
class PeopleListViewModel @Inject constructor(
    private var peopleRepository: PeopleRepository,
    mainDispatcher: CoroutineDispatcher,
) : ViewModel() {
    private val job = SupervisorJob()
    private val uiScope = CoroutineScope(mainDispatcher + job)
    private var _pagingData: MutableStateFlow<PagingData<Person>>  = MutableStateFlow(PagingData.empty())
    val pagingData: Flow<PagingData<Person>> = _pagingData
    var isInternetAvailable = MutableStateFlow(true)

    init { _pagingData = getUsers() }

    private fun getUsers(): MutableStateFlow<PagingData<Person>> {
        uiScope.launch {
            isInternetAvailable.collectLatest {
                if (it)
                    peopleRepository.getUsersRemote().cachedIn(uiScope).collect { pagingData ->
                        _pagingData.emit(pagingData)
                    }
                else
                    peopleRepository.getUsersLocal().cachedIn(uiScope).collect { pagingData ->
                        _pagingData.emit(pagingData)
                    }
            }
        }
        return _pagingData
    }

    override fun onCleared() {
        super.onCleared()
        job.cancel()
    }
}