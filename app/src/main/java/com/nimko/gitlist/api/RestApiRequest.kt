package com.nimko.gitlist.api


import com.nimko.gitlist.api.dto.Person
import com.nimko.gitlist.api.dto.Repo
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface RestApiRequest {
    @GET("users")
    suspend fun getPersons(@Query("per_page") pp:Int, @Query("since") since:Int): List<Person>

    @GET("users/{login}/repos")
    suspend fun getRepoByPersonLogin(@Path("login") login:String,
     @Query("per_page") pp:Int, @Query("page") p:Int): List<Repo>
}