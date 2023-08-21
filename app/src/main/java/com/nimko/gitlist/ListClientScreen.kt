package com.nimko.gitlist

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowLeft
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.LifecycleOwner
import com.nimko.gitlist.dbservices.entitys.Client
import com.nimko.gitlist.dbservices.entitys.ClientRepo
import com.nimko.gitlist.ui.theme.Pink80
import com.nimko.gitlist.ui.theme.Purple80
import com.nimko.gitlist.viewmodel.MyViewModel


@SuppressLint("UnrememberedMutableState")
@Composable
fun ListClient (onClick: () -> Unit, model: MyViewModel, owner: LifecycleOwner){
    val mutableStateListUser = remember {
        mutableStateListOf<Client>()
    }
    model.clients.observe(owner,{
            mutableStateListUser.clear()
            mutableStateListUser.addAll(it)
        Log.d("MyList of Client", it.toString())
    })
    model.clientRepos.observe(owner, {
        Log.d("MyList of Repo ", it.toString())
    })
    model.updateClients()
    Column(modifier = Modifier.fillMaxSize())
    {
        Text(
            text = "Git clients:",
            modifier = Modifier
                .fillMaxWidth(),
            fontSize = 25.sp,
        )
        LazyColumn(
            modifier = Modifier.fillMaxSize()
        ) {
            itemsIndexed(mutableStateListUser) { i, item ->
                ClientListItems(onClick, item, model)
            }
        }
    }
}

@Composable
fun ClientListItems(onClick: () -> Unit, item: Client, model: MyViewModel) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(1.dp),
        shape = RoundedCornerShape(15.dp)
    ) {
        Text(
            text = "${item.id} ${item.login}",
            fontSize= 30.sp,
            color = Color.Yellow,
            modifier = Modifier
                .fillMaxWidth()
                .background(color = Color.Blue)
                .padding(10.dp)
                .clickable {
                    Log.d("My Log", "Click ${item.id} ${item.login}")

                  //  model.isRepo.postValue(true)
                    model.updateClientRepos(item.login)
                    onClick()
                }
        )
    }

}

@SuppressLint("UnrememberedMutableState")
@Composable
fun ListClientRepos (onClick: () -> Unit, model: MyViewModel, owner: LifecycleOwner){
    val mutableStateListRepo = remember {
        mutableStateListOf<ClientRepo>()
    }
    model.clientRepos.observe(owner, {
        mutableStateListRepo.clear()
        mutableStateListRepo.addAll(it)
        Log.d("MyList of Repo ", it.toString())
    })
    model.updateClients()
    Column(modifier = Modifier.fillMaxSize())
    {
        Row(modifier = Modifier.fillMaxWidth())
            {
                IconButton(
                    onClick = {
                        onClick()
                        //model.isRepo.postValue(false)
                    }
                ) {
                    Icon(
                        Icons.Filled.KeyboardArrowLeft,
                        contentDescription = "Back",
                        modifier = Modifier.size(80.dp),
                        tint = Color.Black
                    )
                }
                Text(
                    text = "Repos by ",

                    fontSize = 25.sp,
                )
                Text(
                    text = "${mutableStateListRepo.get(0).clientLogin}:",
                    color = Color.Red,
                    fontSize = 25.sp,
                )
            }
        LazyColumn(
            modifier = Modifier.fillMaxSize()
        ) {
            itemsIndexed(mutableStateListRepo) { i, item ->
                ClientRepoListItems(item, model)
            }
        }
    }
}


@Composable
fun ClientRepoListItems(item: ClientRepo, model: MyViewModel) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(1.dp),
        shape = RoundedCornerShape(15.dp)
    ) {
        Text(
            text = "${item.id} ${item.name}",
            fontSize= 30.sp,
            color = Color.Black,
            modifier = Modifier
                .fillMaxWidth()
                .background(color = Pink80)
                .padding(10.dp)
                .clickable {
                    Log.d("My Log", "Click ${item.id} ${item.name}")
                }
        )
    }

}

