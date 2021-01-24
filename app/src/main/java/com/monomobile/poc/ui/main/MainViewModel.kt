package com.monomobile.poc.ui.main

import androidx.lifecycle.ViewModel
import com.monomobile.poc.api.ApiRepository
import com.monomobile.poc.api.Resource
import androidx.lifecycle.*
import kotlinx.coroutines.Dispatchers

class MainViewModel(private val apiRepository : ApiRepository) : ViewModel() {

    private val message = MutableLiveData<String>()

    fun getSearchResults() {
        message.value = "init"
    }

    var artists = message.switchMap { message ->
        liveData(Dispatchers.IO) {
            emit(Resource.loading(message))
            emit(apiRepository.getSearchResults())
        }
    }

}
