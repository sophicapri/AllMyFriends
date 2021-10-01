package com.example.allmyfriends.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.paging.PagingData
import com.example.allmyfriends.model.Person
import com.example.allmyfriends.repository.RemoteDataRepository
import com.example.allmyfriends.util.Result
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import androidx.paging.cachedIn
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainActivityViewModel @Inject constructor(
    private var remoteDataRepository: RemoteDataRepository,
    var mainDispatcher: CoroutineDispatcher
) : ViewModel() {
    private val job = SupervisorJob()
    private val uiScope = CoroutineScope(mainDispatcher + job)

    fun getUsers(): LiveData<Result<PagingData<Person>>> {
           return remoteDataRepository.getUsers().asLiveData()
    }
}