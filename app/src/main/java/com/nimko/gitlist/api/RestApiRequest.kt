package com.nimko.gitlist.api


import com.nimko.gitlist.api.dto.Person
import com.nimko.gitlist.api.dto.Repo
import retrofit2.http.GET
import retrofit2.http.Path

interface RestApiRequest {
    @GET("users")
    suspend fun getPersons(): List<Person>

    @GET("users/{login}/repos")
    suspend fun getRepoByPersonLogin(@Path("login") login:String): List<Repo>
}