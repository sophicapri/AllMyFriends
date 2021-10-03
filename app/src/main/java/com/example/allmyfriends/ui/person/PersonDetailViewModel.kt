package com.example.allmyfriends.ui.person

import androidx.lifecycle.ViewModel
import com.example.allmyfriends.model.Person
import com.example.allmyfriends.repository.PeopleRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

@HiltViewModel
class PersonDetailViewModel @Inject constructor(
    private var peopleRepository: PeopleRepository
): ViewModel() {

}