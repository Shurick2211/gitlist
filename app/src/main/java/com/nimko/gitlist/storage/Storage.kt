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
        val list = dao.getAllClient(perPage, page*perPage)
        Log.d("STORAGE","Clients: ${list.size}, per_page:$perPage")
        if ( list.size <= perPage) {
            val since = if (list.size != 0) list.get(list.lastIndex).id.toInt() else
                if (page == 0) 0 else 1
            Log.d("Storage", "NEED Client DATA! With id:$since")
            saveDbClientFromApi(perPage, since)
            return dao.getAllClient(perPage, page*perPage).toMutableList()
        }
        return list.toMutableList()
    }

    suspend fun getClientRepo(login: String, perPage:Int, page:Int):MutableList<ClientRepo>{
        val list = dao.getAllClientRepos(login, perPage, page*perPage)
        if (list.size <= perPage) {
            Log.d("Storage", "NEED Repo DATA!")
            saveDbClientRepoFromApi(login, perPage, page)
            return dao.getAllClientRepos(login, perPage, page*perPage).toMutableList()
        }
        return list.toMutableList()
    }

    private suspend fun saveDbClientFromApi(perPage:Int, since:Int){
        try {
            api.getClients(perPage,since).forEach {
                try{
                    dao.saveClient(it)
                    Log.d("Db", "Client $it - save!")
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
            api.getClientRepos(login, perPage, page+1).forEach {
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