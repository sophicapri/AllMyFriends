package com.example.allmyfriends.ui

import android.util.Log
import androidx.lifecycle.*
import com.example.allmyfriends.model.User
import com.example.allmyfriends.repository.PeopleRepository
import com.example.allmyfriends.util.Result
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import javax.inject.Inject

@HiltViewModel
class MainActivityViewModel @Inject constructor(
    private var peopleRepository: PeopleRepository,
    var mainDispatcher: CoroutineDispatcher
    ) : ViewModel() {
    private val job = SupervisorJob()
    private val uiScope = CoroutineScope(mainDispatcher + job)

    fun getUsers(page: Int): LiveData<Result<List<User>>> {
           return peopleRepository.getUsers(page).asLiveData()
    }
}