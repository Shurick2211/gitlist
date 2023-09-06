package com.nimko.gitlist.storage

import android.database.sqlite.SQLiteConstraintException
import android.util.Log
import com.nimko.gitlist.api.ApiService
import com.nimko.gitlist.dbservices.dao.ClientDao
import com.nimko.gitlist.dbservices.entitys.Client
import com.nimko.gitlist.dbservices.entitys.ClientRepo
import com.nimko.gitlist.viewmodel.MyViewModel
import retrofit2.HttpException

class Storage(
    private val dao: ClientDao,
    private val api: ApiService,
    private val viewModel: MyViewModel
) {
    suspend fun getClient(perPage: Int, page: Int): MutableList<Client> {
        val list = dao.getAllClient(perPage, page * perPage - 1)
        Log.d("STORAGE", "Clients: ${list.size}, per_page:$perPage")
        if (list.size < perPage) {
            val since = if (list.isNotEmpty()) list[list.lastIndex].id.toInt() else 0
            Log.d("Storage", "NEED Client DATA! With id:$since")
            saveDbClientFromApi(perPage, since)
            return dao.getAllClient(perPage, page * perPage).toMutableList()
        }
        return list.toMutableList()
    }

    suspend fun getClientRepo(perPage: Int, page: Int): MutableList<ClientRepo> {
        val login = viewModel.login.value!!
        val list = dao.getAllClientRepos(login, perPage, page * perPage - 1)
        if (list.size < perPage) {
            Log.d("Storage", "NEED Repo DATA!")
            saveDbClientRepoFromApi(login, perPage, page)
            return dao.getAllClientRepos(login, perPage, page * perPage).toMutableList()
        }
        return list.toMutableList()
    }

    suspend fun getSearchClient(perPage: Int, page: Int): List<Client> {
        val list = getSearchClientOnDb(perPage, page)
        return if (list.size < MIN_FOR_GET_SEARCH)
                    (list + getSearchClientOnApi(perPage, page)).distinct()
               else list
    }

    private suspend fun getSearchClientOnApi(perPage: Int, page: Int): List<Client> {
        val searchBy = viewModel.searchUserBy.value!!
        val listFromApi =
            try {
                api.searchClient(searchBy, perPage, page)
            } catch (he: HttpException) {
                Log.d("ApiError", he.toString())
                emptyList()
            }
        Log.d("STORAGE Search", "Clients API:${listFromApi.size} for $searchBy")
        saveListClientsOnDb(listFromApi)
        return listFromApi
    }

    private suspend fun getSearchClientOnDb(perPage: Int, page: Int): List<Client> {
        val searchBy = viewModel.searchUserBy.value!!
        return dao.getAllClient(perPage = perPage, page * perPage - 1, search = searchBy)
    }

    private suspend fun saveListClientsOnDb(list: List<Client>) {
        list.forEach {
            try {
                dao.saveClient(it)
                Log.d("Db", "Client $it - save!")
            } catch (e: SQLiteConstraintException) {
                Log.d("DbError", "Client $it - existed!")
            }
        }
    }

    private suspend fun saveDbClientFromApi(perPage: Int, since: Int) {
        try {
            api.getClients(perPage, since).forEach {
                try {
                    dao.saveClient(it)
                    Log.d("Db", "Client $it - save!")
                } catch (e: SQLiteConstraintException) {
                    Log.d("DbError", "Client $it - existed!")
                }
            }
        } catch (he: HttpException) {
            Log.d("ApiError", he.toString())
        }
    }

    private suspend fun saveDbClientRepoFromApi(login: String, perPage: Int, page: Int) {
        try {
            api.getClientRepos(login, perPage, page + 1).forEach {
                try {
                    dao.saveClientRepo(it)
                    Log.d("Db", "Repo $it - save!")
                } catch (ee: SQLiteConstraintException) {
                    Log.d("DbError", "Repo $it - existed!")
                }
            }
        } catch (he: HttpException) {
            Log.d("ApiError", he.toString())
        }
    }

    companion object {
        const val MIN_FOR_GET_SEARCH = 5
    }

}