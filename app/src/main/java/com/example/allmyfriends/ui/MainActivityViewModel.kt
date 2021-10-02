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
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.distinctUntilChangedBy
import javax.inject.Inject

@HiltViewModel
class MainActivityViewModel @Inject constructor(
    private var peopleRepository: PeopleRepository,
    var mainDispatcher: CoroutineDispatcher
) : ViewModel() {
    private val job = SupervisorJob()
    private val uiScope = CoroutineScope(mainDispatcher + job)

    fun getUsers(): LiveData<PagingData<Person>> {
        return peopleRepository.getUsers()
            .cachedIn(uiScope).asLiveData()
    }
}