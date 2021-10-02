package com.example.allmyfriends.ui

import androidx.lifecycle.*
import androidx.paging.ExperimentalPagingApi
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.allmyfriends.model.Person
import com.example.allmyfriends.repository.PeopleRepository
import com.example.allmyfriends.util.Result
import dagger.assisted.Assisted
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.*
import javax.inject.Inject

@HiltViewModel
class MainActivityViewModel @Inject constructor(
    private var peopleRepository: PeopleRepository,
    var mainDispatcher: CoroutineDispatcher,
    private var savedStateHandle: SavedStateHandle

) : ViewModel() {
    private val job = SupervisorJob()
    private val uiScope = CoroutineScope(mainDispatcher + job)
    var pagingData: Flow<PagingData<Person>>

    init {
        pagingData = (savedStateHandle.get<Flow<PagingData<Person>>>("PAGING_DATA") ?: MutableStateFlow(
                PagingData.empty()
            ))
    }

    fun getUsers(): Flow<PagingData<Person>> {
        pagingData = if (internetAvailable())
            peopleRepository.getUsers()
                .cachedIn(uiScope).apply {
                    pagingData = this
                } else
            peopleRepository.getUsersLocal()
                .cachedIn(uiScope)

        return pagingData
    }

    private fun internetAvailable() = true

    override fun onCleared() {
        super.onCleared()
        savedStateHandle["PAGING_DATA"] = pagingData
    }
}