package com.nimko.gitlist.dbservices.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.nimko.gitlist.dbservices.entitys.Client
import com.nimko.gitlist.dbservices.entitys.ClientRepo

@Dao
interface ClientDao {
    @Query("SELECT * FROM clients LIMIT :perPage OFFSET :offset")
    suspend fun getAllClient(perPage:Int, offset:Int):List<Client>

    @Query("SELECT * FROM client_repos WHERE client_login = :login " +
            "LIMIT :perPage OFFSET :offset")
    suspend fun getAllClientRepos(login:String, perPage:Int, offset:Int):List<ClientRepo>

    @Insert(entity = Client::class)
    suspend fun saveClient(client: Client)

    @Insert(entity = ClientRepo::class)
    suspend fun saveClientRepo(repo: ClientRepo)
}