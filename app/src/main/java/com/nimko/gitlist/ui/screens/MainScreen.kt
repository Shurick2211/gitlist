package com.nimko.gitlist.ui.screens

import android.util.Log
import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.nimko.gitlist.viewmodel.MyViewModel

@Composable
fun MainScreen(model: MyViewModel){
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = Screens.LIST_USER.value){
        composable(Screens.LIST_USER.value){
            ListClient(onClick = {
                model.login.postValue(it)
                Log.d("SCREEN", "${Screens.LIST_USER.value} for $it")
                navController.navigate(Screens.LIST_USER_REPO.value)
            }, model)

        }
        composable(Screens.LIST_USER_REPO.value){
            ListClientRepos(onClick = {
                Log.d("SCREEN", "event: $it; ${Screens.LIST_USER_REPO.value}")
                when(it){
                    0 -> navController.navigateUp()
                    1 -> navController.navigate(Screens.WEB_SCREEN.value)
                }
            },model)
        }
        composable(Screens.WEB_SCREEN.value){
            WebScreen(onClick = { navController.navigateUp() }, model = model )
        }
    }
}



enum class Screens(val value:String){
    LIST_USER("listUser"),
    LIST_USER_REPO("listUserRepo"),
    WEB_SCREEN("webScreen")
}
