package com.nimko.gitlist.api

import com.nimko.gitlist.dbservices.entitys.Client
import com.nimko.gitlist.dbservices.entitys.ClientRepo
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class ApiService {
    private val retrofit = Retrofit.Builder()
     .baseUrl("https://api.github.com/")
     .addConverterFactory(GsonConverterFactory.create())
     .build()
    private val restApi = retrofit.create(RestApiRequest::class.java)

    suspend fun getClients(pp: Int, since: Int): List<Client> {
        val persons = restApi.getPersons(pp, since)
        return persons.map { it.toEntity() }
    }


    suspend fun getClientRepos(login:String, pp: Int, p: Int) : List<ClientRepo> {
        val repos = restApi.getRepoByPersonLogin(login = login, pp, p)
        return  repos.map { it.toEntity() }
    }

}