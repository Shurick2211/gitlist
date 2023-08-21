package com.nimko.gitlist

import android.annotation.SuppressLint
import android.os.Bundle
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
//                   var isR = mutableStateOf(false)
//                    model.isRepo.observe(this,{
//                        runOnUiThread {
//                           isR.value = it
//                        }
//                    })
//                    if(isR.value)
//                        ListClientRepos(model = model, owner = this)
//                    else
//                        ListClient(model = model, owner = this )

                    val navController = rememberNavController()

                    NavHost(navController = navController, startDestination = LIST_USER ){
                        composable(LIST_USER){
                            ListClient(onClick = {
                                navController.navigate(LIST_USER_REPO)
                            }, model = model, owner = this@MainActivity)

                        }
                        composable(LIST_USER_REPO){
                            ListClientRepos(onClick = {
                                navController.navigate(LIST_USER)
                            }, model = model, owner = this@MainActivity)

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



