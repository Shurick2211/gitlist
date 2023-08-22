package com.nimko.gitlist

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.nimko.gitlist.dbservices.entitys.Client
import com.nimko.gitlist.dbservices.entitys.ClientRepo
import com.nimko.gitlist.ui.theme.GitlistTheme
import com.nimko.gitlist.viewmodel.MyViewModel


class MainActivity : ComponentActivity() {

    private val model: MyViewModel by viewModels { MyViewModel.factory }

    @SuppressLint("UnrememberedMutableState")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        model.updateClients()

        setContent {
            GitlistTheme {

                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                )
                {
                    val mutableStateListRepo = remember {
                        mutableStateListOf<ClientRepo>()
                    }
                    model.clientRepos.observe(this, {
                        mutableStateListRepo.clear()
                        mutableStateListRepo.addAll(it)
                        Log.d("MyList of Repo ", it.toString())
                    })

                    val mutableStateListUser = remember {
                        mutableStateListOf<Client>()
                    }
                    model.clients.observe(this,{
                        mutableStateListUser.clear()
                        mutableStateListUser.addAll(it)
                        Log.d("MyList of Client", it.toString())
                    })

                    val navController = rememberNavController()

                    NavHost(navController = navController, startDestination = LIST_USER ){
                        composable(LIST_USER){
                            ListClient(onClick = {
                                model.updateClientRepos(it)
                                Log.d("SCREEN", LIST_USER)
                                navController.navigate(LIST_USER_REPO)
                            }, mutableStateListUser)

                        }
                        composable(LIST_USER_REPO){
                            ListClientRepos(onClick = {
                                Log.d("SCREEN", LIST_USER_REPO)
                                navController.navigate(LIST_USER)
                            }, mutableStateListRepo)
                        }
                    }
                }
            }
        }
    }

    companion object{
         val LIST_USER = "listUser"
         val LIST_USER_REPO = "listUserRepo"
    }
}



