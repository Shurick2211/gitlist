package com.nimko.gitlist.viewmodel

import android.database.sqlite.SQLiteConstraintException
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewmodel.CreationExtras
import com.nimko.gitlist.dbservices.App
import com.nimko.gitlist.api.ApiService
import com.nimko.gitlist.dbservices.dao.Db
import com.nimko.gitlist.dbservices.entitys.Client
import com.nimko.gitlist.dbservices.entitys.ClientRepo
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import retrofit2.HttpException

class MyViewModel (val database:Db) : ViewModel() {
    var clients: MutableLiveData<MutableList<Client>> = MutableLiveData()
    var clientRepos: MutableLiveData<MutableList<ClientRepo>> = MutableLiveData()


    val api = ApiService()
    val dao = database.getClintDao()

    var isApiUpdated = false
    fun saveDb(){
        isApiUpdated = true
        GlobalScope.launch(Dispatchers.IO) {
            try {
                api.getClients(5,1).forEach {
                    try {
                        api.getClientRepos(it.login, 3, 1).forEach {
                            try {
                                dao.saveClientRepo(it)
                                Log.d("Db", "Repo $it - save!")
                            } catch (ee: SQLiteConstraintException) {
                                Log.d("DbError", "Repo $it - existed!")
                            }
                        }
                        dao.saveClient(it)
                    } catch (e: SQLiteConstraintException) {
                        Log.d("DbError", "Client $it - existed!")
                    }
                }
                updateClients()
            }catch (he: HttpException){
                Log.d("ApiError", he.toString())
            }
        }
    }

    fun updateClients(){
        GlobalScope.launch(Dispatchers.IO) {
            clients.postValue(dao.getAllClient().toMutableList())
        }
        if (!isApiUpdated) {
            Log.d("SAVE", "me")
            saveDb()
        }

    }

    fun updateClientRepos(login:String){
        GlobalScope.launch(Dispatchers.IO) {
            clientRepos.postValue(dao.getAllClientRepos(login).toMutableList())
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