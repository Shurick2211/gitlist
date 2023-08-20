package com.nimko.gitlist.api

import com.nimko.gitlist.dbservices.entitys.Client
import com.nimko.gitlist.dbservices.entitys.ClientRepo
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class ApiService {
 val retrofit = Retrofit.Builder()
     .baseUrl("https://api.github.com/")
     .addConverterFactory(GsonConverterFactory.create())
     .build()
val restApi = retrofit.create(RestApiRequest::class.java)

 suspend fun getClients() : List<Client> {
     val persons = restApi.getPersons()
     return persons.map { it.toEntity() }
 }


 suspend fun getClientRepos(login:String) : List<ClientRepo> {
     val repos = restApi.getRepoByPersonLogin(login = login)
     return  repos.map { it.toEntity() }
 }

}