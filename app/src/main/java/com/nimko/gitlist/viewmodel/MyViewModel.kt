package com.nimko.gitlist.viewmodel

import android.annotation.SuppressLint
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
import com.nimko.gitlist.App
import com.nimko.gitlist.dbservices.Db
import com.nimko.gitlist.storage.Storage
import com.nimko.gitlist.storage.paging.PagingSource

@SuppressLint("StaticFieldLeak")
class MyViewModel (context: Context) : ViewModel() {
    private val storage =
        Storage(Db.createDataBase(context).getClintDao(), ApiService(), this)
    var clientFlow = getClientPager()
    val login: MutableLiveData<String> = MutableLiveData("")
    val url: MutableLiveData<String> = MutableLiveData("")
    val searchUserBy: MutableLiveData<String> = MutableLiveData("")

    fun getClientPager() = Pager(
        config = PagingConfig(PAGE_SIZE_CLIENT, enablePlaceholders = false),
        pagingSourceFactory = {
                PagingSource(storage::getClient, PAGE_SIZE_CLIENT)
        }
    ).flow.cachedIn(viewModelScope)

    fun getClientSearchPager() = Pager(
        config = PagingConfig(PAGE_SIZE_CLIENT, enablePlaceholders = false),
        pagingSourceFactory = {
            PagingSource(storage::getSearchClient, PAGE_SIZE_CLIENT)
        }
    ).flow.cachedIn(viewModelScope)

    fun getClientRepoPager() = Pager(
            config = PagingConfig(PAGE_SIZE_CLIENT_REPO, enablePlaceholders = false),
            pagingSourceFactory = {
                PagingSource(storage::getClientRepo, PAGE_SIZE_CLIENT_REPO)
            }
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