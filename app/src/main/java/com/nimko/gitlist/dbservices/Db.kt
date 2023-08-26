package com.nimko.gitlist.dbservices


import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.nimko.gitlist.dbservices.dao.ClientDao
import com.nimko.gitlist.dbservices.entitys.Client
import com.nimko.gitlist.dbservices.entitys.ClientRepo


@Database(
    entities = [
        Client::class,
        ClientRepo::class
    ],
    version = 2
)
abstract class Db: RoomDatabase() {
    abstract fun getClintDao(): ClientDao

    companion object{
        fun createDataBase(context: Context): Db {
            return Room.databaseBuilder(
                context,
                Db::class.java,
                "gitlist-db.db"
            ).build()
        }
    }
}