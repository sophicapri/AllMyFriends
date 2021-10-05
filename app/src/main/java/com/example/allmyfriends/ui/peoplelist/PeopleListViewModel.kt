package com.example.allmyfriends.ui.peoplelist

import androidx.lifecycle.*
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.allmyfriends.model.Person
import com.example.allmyfriends.repository.PeopleRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import javax.inject.Inject


@HiltViewModel
class PeopleListViewModel @Inject constructor(
    private var peopleRepository: PeopleRepository,
    mainDispatcher: CoroutineDispatcher,
) : ViewModel() {
    private val job = SupervisorJob()
    private val uiScope = CoroutineScope(mainDispatcher + job)
    private var _pagingData: MutableStateFlow<PagingData<Person>>  = MutableStateFlow(PagingData.empty())
    val pagingData: Flow<PagingData<Person>> = _pagingData
    val isInternetAvailable = MutableStateFlow(false)

    init { _pagingData = getPeople() }

    private fun getPeople(): MutableStateFlow<PagingData<Person>> {
        uiScope.launch {
            isInternetAvailable.collectLatest {
                if (it)
                    peopleRepository.getPeopleRemote().cachedIn(uiScope).collect { pagingData ->
                        _pagingData.emit(pagingData)
                    }
                else
                    peopleRepository.getPeopleLocal().cachedIn(uiScope).collect { pagingData ->
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

    companion object {
        private const val TAG = "PeopleListViewModel"
    }
}