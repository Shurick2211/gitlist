package com.nimko.gitlist.api

import com.nimko.gitlist.dbservices.entitys.Client
import com.nimko.gitlist.dbservices.entitys.ClientRepo
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Query

class ApiService {
    private val restApi = Retrofit.Builder()
     .baseUrl("https://api.github.com/")
     .addConverterFactory(GsonConverterFactory.create())
     .build().create(RestApiRequest::class.java)

    suspend fun getClients(pp: Int, since: Int): List<Client>  =
        restApi.getPersons(pp, since).map { it.toEntity() }

    suspend fun getClientRepos(login:String, pp: Int, p: Int) : List<ClientRepo>
    = restApi.getRepoByPersonLogin(login = login, pp, p).map { it.toEntity() }

    suspend fun searchClient(searchBy:String, perPage:Int, page:Int): List<Client> =
        restApi.searchPersons(searchBy, perPage, page).getSearchListOfClient()

}