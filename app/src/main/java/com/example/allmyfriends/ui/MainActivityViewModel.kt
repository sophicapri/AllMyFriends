package com.example.allmyfriends.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.paging.PagingData
import com.example.allmyfriends.model.Person
import com.example.allmyfriends.repository.PeopleRepository
import com.example.allmyfriends.util.Result
import dagger.assisted.Assisted
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import javax.inject.Inject

@HiltViewModel
class MainActivityViewModel @Inject constructor(
    private var peopleRepository: PeopleRepository,
    var mainDispatcher: CoroutineDispatcher,
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {
    private val job = SupervisorJob()
    private val uiScope = CoroutineScope(mainDispatcher + job)

    fun getUsers(): LiveData<Result<PagingData<Person>>> {
           return peopleRepository.getUsers().asLiveData()
    }
}