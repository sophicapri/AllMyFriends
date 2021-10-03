package com.example.allmyfriends.ui

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkInfo
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
import android.net.NetworkCapabilities

import android.net.Network

import android.os.Build
import android.util.Log
import kotlinx.coroutines.launch
import ru.beryukhov.reactivenetwork.ReactiveNetwork
import java.lang.NullPointerException


@HiltViewModel
class MainActivityViewModel @Inject constructor(
    private var peopleRepository: PeopleRepository,
    mainDispatcher: CoroutineDispatcher,
) : ViewModel() {
    private val job = SupervisorJob()
    private val uiScope = CoroutineScope(mainDispatcher + job)
    var _pagingData: MutableStateFlow<PagingData<Person>>  = MutableStateFlow(PagingData.empty())
    val pagingData: Flow<PagingData<Person>> = _pagingData
    var isInternetAvalaible = MutableStateFlow(true)

    init { _pagingData = getUsers() }

    private fun getUsers(): MutableStateFlow<PagingData<Person>> {
        uiScope.launch {
            isInternetAvalaible.collectLatest {
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