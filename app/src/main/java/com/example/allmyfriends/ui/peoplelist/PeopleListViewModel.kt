package com.example.allmyfriends.ui.peoplelist

import androidx.lifecycle.ViewModel
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.allmyfriends.model.Person
import com.example.allmyfriends.repository.PeopleRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class PeopleListViewModel @Inject constructor(
    private var peopleRepository: PeopleRepository,
    mainDispatcher: CoroutineDispatcher,
) : ViewModel() {
    private val job = SupervisorJob()
    private val uiScope = CoroutineScope(mainDispatcher + job)
    private var _pagingData: MutableStateFlow<PagingData<Person>> =
        MutableStateFlow(PagingData.empty())
    val pagingData: Flow<PagingData<Person>> = _pagingData
    private val _isInternetAvailable = MutableStateFlow(true)
    val isInternetAvailable : StateFlow<Boolean> = _isInternetAvailable

    init { _pagingData = getPeople() }

    fun changeConnectivityStatus(isConnected: Boolean) {
        uiScope.launch {
            _isInternetAvailable.emit(isConnected)
        }
    }

    fun getPeople(): MutableStateFlow<PagingData<Person>> {
        uiScope.launch {
            isInternetAvailable.collectLatest { available ->
                if (available)
                    peopleRepository.getPeopleRemote().cachedIn(uiScope).collect {
                        _pagingData.emit(it)
                    }
                else
                    peopleRepository.getPeopleLocal().cachedIn(uiScope).collect {
                        _pagingData.emit(it)
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