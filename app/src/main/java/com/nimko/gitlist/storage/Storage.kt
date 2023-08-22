package com.nimko.gitlist.storage

import android.database.sqlite.SQLiteConstraintException
import android.util.Log
import com.nimko.gitlist.api.ApiService
import com.nimko.gitlist.dbservices.dao.ClientDao
import com.nimko.gitlist.dbservices.entitys.Client
import com.nimko.gitlist.dbservices.entitys.ClientRepo
import retrofit2.HttpException

class Storage (
    val dao:ClientDao,
    val api:ApiService
){

    suspend fun getClient(perPage:Int, page:Int):MutableList<Client>{
        saveDbClientFromApi(perPage, page)
        return dao.getAllClient(perPage, page*perPage).toMutableList()
    }

    suspend fun getClientRepo(login: String, perPage:Int, page:Int):MutableList<ClientRepo>{
        saveDbClientRepoFromApi(login, perPage, page)
        return dao.getAllClientRepos(login, perPage, page*perPage).toMutableList()
    }

    private suspend fun saveDbClientFromApi(perPage:Int, page:Int){
        try {
            api.getClients(perPage,page).forEach {
                try{
                    dao.saveClient(it)
                } catch (e: SQLiteConstraintException) {
                    Log.d("DbError", "Client $it - existed!")
                }
            }
        }catch (he: HttpException){
            Log.d("ApiError", he.toString())
        }
    }

    private suspend fun saveDbClientRepoFromApi(login: String, perPage:Int, page:Int){
        try {
            api.getClientRepos(login, perPage, page).forEach {
                try {
                    dao.saveClientRepo(it)
                    Log.d("Db", "Repo $it - save!")
                } catch (ee: SQLiteConstraintException) {
                    Log.d("DbError", "Repo $it - existed!")
                }
            }
        }catch (he: HttpException){
            Log.d("ApiError", he.toString())
        }
    }

}