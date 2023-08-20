package com.nimko.gitlist.dbservices.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.nimko.gitlist.dbservices.entitys.Client
import com.nimko.gitlist.dbservices.entitys.ClientRepo

@Dao
interface ClientDao {
    @Query("SELECT * FROM clients")
    fun getAllClient():List<Client>

    @Query("SELECT * FROM client_repos WHERE client_login = :login")
    fun getAllClientRepos(login:String):List<ClientRepo>

    @Insert(entity = Client::class)
    fun saveClient(client: Client)

    @Insert(entity = ClientRepo::class)
    fun saveClientRepo(repo: ClientRepo)
}