package com.nimko.gitlist.dbservices

import android.app.Application
import com.nimko.gitlist.dbservices.dao.Db

class App: Application() {
    val database by lazy { Db.createDataBase(this) }
}