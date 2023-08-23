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
                    mainScreen(model = model)
                }
            }
        }
    }

    companion object{
        const val LIST_USER = "listUser"
        const val LIST_USER_REPO = "listUserRepo"

    }
}



