package com.example.allmyfriends.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.example.allmyfriends.model.ApiResponse
import com.example.allmyfriends.repository.RemoteDataRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainActivityViewModel @Inject constructor(
    private var remoteDataRepository: RemoteDataRepository,
    var mainDispatcher: CoroutineDispatcher
) : ViewModel() {
    private val job = SupervisorJob()
    private val uiScope = CoroutineScope(mainDispatcher + job)

    fun getDataFromRemote(): LiveData<ApiResponse> {
        //uiScope.launch {  }
       return remoteDataRepository.getDataFromRemote().asLiveData()
    }
}