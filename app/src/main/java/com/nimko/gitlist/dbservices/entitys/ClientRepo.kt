package com.nimko.gitlist.dbservices.entitys

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(tableName = "client_repos")
data class ClientRepo(
    @PrimaryKey val id: Long,
    val name: String,
    @ColumnInfo(name="client_login") val clientLogin:String
)
