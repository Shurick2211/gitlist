package com.nimko.gitlist.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.CreationExtras
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.cachedIn
import com.nimko.gitlist.api.ApiService
import com.nimko.gitlist.dbservices.App
import com.nimko.gitlist.dbservices.dao.Db
import com.nimko.gitlist.storage.ClientPagingSource
import com.nimko.gitlist.storage.ClientRepoPagingSource
import com.nimko.gitlist.storage.Storage

class MyViewModel (val database:Db) : ViewModel() {
    private val storage = Storage(database.getClintDao(), ApiService())

    fun getClientPager(perPage:Int) = Pager(
        config = PagingConfig(perPage, enablePlaceholders = false),
        pagingSourceFactory = {ClientPagingSource(storage, perPage)}
    ).flow.cachedIn(viewModelScope)


    fun getClientRepoPager(login:String, perPage: Int) = Pager(
            config = PagingConfig(perPage, enablePlaceholders = false),
            pagingSourceFactory = { ClientRepoPagingSource(storage, perPage, login) }
        ).flow.cachedIn(viewModelScope)


    companion object{
        val factory: ViewModelProvider.Factory = object : ViewModelProvider.Factory{
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(
                modelClass: Class<T>,
                extras: CreationExtras): T {
                val database = (checkNotNull(extras[APPLICATION_KEY]) as App).database
                return MyViewModel(database) as T
            }
        }
    }

}