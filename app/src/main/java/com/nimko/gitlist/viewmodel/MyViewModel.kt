package com.nimko.gitlist.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewmodel.CreationExtras
import com.nimko.gitlist.api.ApiService
import com.nimko.gitlist.dbservices.App
import com.nimko.gitlist.dbservices.dao.Db
import com.nimko.gitlist.dbservices.entitys.Client
import com.nimko.gitlist.dbservices.entitys.ClientRepo
import com.nimko.gitlist.storage.Storage
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class MyViewModel (val database:Db) : ViewModel() {
    var clients: MutableLiveData<MutableList<Client>> = MutableLiveData()
    var clientRepos: MutableLiveData<MutableList<ClientRepo>> = MutableLiveData()

    val storage = Storage(database.getClintDao(), ApiService())


    fun updateClients(){
        GlobalScope.launch(Dispatchers.IO) {
            clients.postValue(storage.getClient(5,0))
        }
    }

    fun updateClientRepos(login:String){
        GlobalScope.launch(Dispatchers.IO) {
            clientRepos.postValue(storage.getClientRepo(login,3,0))
        }
    }

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