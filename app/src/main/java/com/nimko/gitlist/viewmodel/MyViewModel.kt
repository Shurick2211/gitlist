package com.nimko.gitlist.viewmodel

import android.content.Context
import androidx.lifecycle.MutableLiveData
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

class MyViewModel (val context: Context) : ViewModel() {
    private val storage = Storage(Db.createDataBase(context).getClintDao(), ApiService())
    val clientFlow = getClientPager()
    val login: MutableLiveData<String> = MutableLiveData("")
    val url: MutableLiveData<String> = MutableLiveData("")
    val startPage = 15
    private fun getClientPager() = Pager(
        config = PagingConfig(PAGE_SIZE_CLIENT, enablePlaceholders = false),
        pagingSourceFactory = {ClientPagingSource(storage, PAGE_SIZE_CLIENT, startPage)}
    ).flow.cachedIn(viewModelScope)

    fun getClientRepoPager() = Pager(
            config = PagingConfig(PAGE_SIZE_CLIENT_REPO, enablePlaceholders = false),
            pagingSourceFactory = {
                ClientRepoPagingSource(storage, PAGE_SIZE_CLIENT_REPO, login.value!!) }
        ).flow.cachedIn(viewModelScope)


    companion object{
        val factory: ViewModelProvider.Factory = object : ViewModelProvider.Factory{
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(
                modelClass: Class<T>,
                extras: CreationExtras): T {
                val context = (checkNotNull(extras[APPLICATION_KEY] as App)).context
                return MyViewModel(context) as T
            }
        }
        const val PAGE_SIZE_CLIENT = 30

        const val PAGE_SIZE_CLIENT_REPO = 10
    }

}