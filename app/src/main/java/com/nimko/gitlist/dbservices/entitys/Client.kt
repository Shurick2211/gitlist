package com.nimko.gitlist.dbservices.entitys

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "clients")
data class Client(
    @PrimaryKey val id: Long,
    val login: String
)
