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
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.nimko.gitlist.ui.theme.GitlistTheme
import com.nimko.gitlist.viewmodel.MyViewModel


class MainActivity : ComponentActivity() {

    private val model: MyViewModel by viewModels { MyViewModel.factory }

    @SuppressLint("UnrememberedMutableState")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            GitlistTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                )
                {
                    val navController = rememberNavController()
                    var login = ""
                    NavHost(navController = navController, startDestination = LIST_USER ){
                        composable(LIST_USER){
                            ListClient(onClick = {
                                login = it
                                Log.d("SCREEN", "$LIST_USER for $it")
                                navController.navigate(LIST_USER_REPO)
                            }, model)

                        }
                        composable(LIST_USER_REPO){
                            ListClientRepos(onClick = {
                                Log.d("SCREEN", LIST_USER_REPO)
                                navController.navigate(LIST_USER)
                            },model, login)
                        }
                    }
                }
            }
        }
    }

    companion object{
        const val LIST_USER = "listUser"
        const val LIST_USER_REPO = "listUserRepo"

    }
}



