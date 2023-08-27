package com.nimko.gitlist.api


import com.nimko.gitlist.api.dto.Person
import com.nimko.gitlist.api.dto.Repo
import com.nimko.gitlist.api.dto.SearchUser
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Path
import retrofit2.http.Query

interface RestApiRequest {
    @Headers("Accept: application/vnd.github+json",
        "X-GitHub-Api-Version: 2022-11-28")
    @GET("users")
    suspend fun getPersons(@Query("per_page") perPage:Int,
                           @Query("page") page:Int): List<Person>

    @Headers("Accept: application/vnd.github+json",
        "X-GitHub-Api-Version: 2022-11-28")
    @GET("users/{login}/repos")
    suspend fun getRepoByPersonLogin(@Path("login") login:String,
                                     @Query("per_page") perPage:Int,
                                     @Query("page") page:Int): List<Repo>

    @Headers("Accept: application/vnd.github+json",
        "X-GitHub-Api-Version: 2022-11-28")
    @GET("search/users")
    suspend fun searchPersons(@Query("q") searchBy:String,
                              @Query("per_page") perPage:Int,
                              @Query("page") page:Int): SearchUser

    @Headers("Accept: application/vnd.github+json",
        "X-GitHub-Api-Version: 2022-11-28")
    @GET("search/repositories")
    suspend fun searchRepo(@Query("q") searchBy:String,
                           @Query("per_page") perPage:Int,
                           @Query("page") page:Int): SearchUser
}