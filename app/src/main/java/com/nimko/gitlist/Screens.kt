package com.nimko.gitlist

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowLeft
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.paging.compose.collectAsLazyPagingItems
import com.nimko.gitlist.dbservices.entitys.Client
import com.nimko.gitlist.dbservices.entitys.ClientRepo
import com.nimko.gitlist.ui.theme.Pink80
import com.nimko.gitlist.viewmodel.MyViewModel

@Composable
fun mainScreen(model: MyViewModel){
    val navController = rememberNavController()
    var login = ""
    NavHost(navController = navController, startDestination = MainActivity.LIST_USER ){
        composable(MainActivity.LIST_USER){
            ListClient(onClick = {
                login = it
                Log.d("SCREEN", "${MainActivity.LIST_USER} for $it")
                navController.navigate(MainActivity.LIST_USER_REPO)
            }, model)

        }
        composable(MainActivity.LIST_USER_REPO){
            ListClientRepos(onClick = {
                Log.d("SCREEN", MainActivity.LIST_USER_REPO)
                navController.navigate(MainActivity.LIST_USER)
            },model, login)
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("UnrememberedMutableState")
@Composable
fun ListClient (onClick: (param:String) -> Unit, model: MyViewModel){

    val listUser
    = model.getClientPager(PAGE_SIZE_CLIENT).collectAsLazyPagingItems()

    Column(modifier = Modifier.fillMaxSize())
    {
        TopAppBar(title =
            {Text(
                text = stringResource(id = R.string.clientscreen),

                fontSize = 25.sp,
            )
            },
            colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                containerColor = Color.Green.copy(alpha = 0.3f)
            )
        )
        LazyColumn(
                    modifier = Modifier.fillMaxSize()
               ) {
                    items(listUser.itemCount) {  item ->
                       ClientListItems(onClick, listUser[item]!!)
                    }
               }
    }
}

@Composable
fun ClientListItems(onClick: (param:String) -> Unit, item: Client) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(1.dp),
        shape = RoundedCornerShape(15.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(color = Color.Blue)
                .clickable {
                    Log.d("My Log", "Click ${item.id} ${item.login}")
                    onClick(item.login)
                }
        ) {
            Text(
                text = stringResource(id = R.string.id),
                fontSize= 16.sp,
                color = Color.Yellow,
                modifier = Modifier
                    .padding(5.dp)
            )
            Text(
                text = "${item.id}",
                fontSize= 30.sp,
                color = Color.Yellow,
                modifier = Modifier
                    .padding(5.dp)
            )
            Text(
                text = stringResource(id = R.string.login),
                fontSize= 16.sp,
                color = Color.Yellow,
                modifier = Modifier
                    .padding(5.dp)
            )
            Text(
                text = "${item.login}",
                fontSize= 30.sp,
                color = Color.Yellow,
                modifier = Modifier
                    .padding(5.dp)
            )

        }

    }

}

@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("UnrememberedMutableState")
@Composable
fun ListClientRepos (onClick: () -> Unit, model: MyViewModel, login:String){
    val listRepo =
        model.getClientRepoPager(login, PAGE_SIZE_CLIENT_REPO).collectAsLazyPagingItems()

    Column(modifier = Modifier.fillMaxSize())
    {
        TopAppBar(title = {
            Row {
                Text(
                    text = stringResource(id = R.string.reposcreen),

                    fontSize = 25.sp,
                )
                Text(
                    text = " $login:" ,
                    color = Color.Red,
                    fontSize = 25.sp,
                )
            }

        },
            navigationIcon = {
            IconButton(
                onClick = {
                    onClick()
                }
            ) {
                Icon(
                    Icons.Filled.KeyboardArrowLeft,
                    contentDescription = "Back",
                    modifier = Modifier.size(80.dp),
                    tint = Color.Black
                )
            }
        },
            colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                containerColor = Color.Green.copy(alpha = 0.3f)
            )
        )
        LazyColumn(
            modifier = Modifier.fillMaxSize()
        ) {
            items(listRepo.itemCount) { item ->
                 ClientRepoListItems(listRepo[item]!!)
            }
        }
    }
}




@Composable
fun ClientRepoListItems(item: ClientRepo) {
    val intent = Intent(Intent.ACTION_VIEW, Uri.parse(item.url))
    val context = LocalContext.current
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(1.dp)
            .clickable {
                Log.d("My Log", "Click ${item.id} ${item.name} ${item.url}")
            }
        ,
        shape = RoundedCornerShape(15.dp)
    ) {
            Column() {
                Row(
                    Modifier
                        .fillMaxWidth()
                        .background(color = Pink80),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = stringResource(id = R.string.id),
                        fontSize= 16.sp,
                        color = Color.Black,
                        modifier = Modifier
                            .padding(start = 30.dp)
                    )
                    Text(
                        text = "${item.id}",
                        fontSize= 30.sp,
                        color = Color.Black,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(start = 3.dp)
                    )
                }
                Row(
                    Modifier
                        .fillMaxWidth()
                        .background(color = Pink80),
                    verticalAlignment = Alignment.CenterVertically

                ) {
                    Text(
                        text = stringResource(id = R.string.name),
                        fontSize= 16.sp,
                        color = Color.Black,
                        modifier = Modifier
                            .padding(3.dp)
                    )
                    Text(
                        text = "${item.name}",
                        fontSize= 30.sp,
                        color = Color.Black,
                        modifier = Modifier
                            .padding(start = 3.dp),


                        )
                    IconButton(onClick = { context.startActivity(intent) },
                        modifier = Modifier.padding(start = 10.dp)
                    ) {
                        Icon(
                            Icons.Filled.KeyboardArrowRight,
                            contentDescription = "Back",
                            modifier = Modifier.size(80.dp),
                            tint = Color.Black
                        )
                    }
                }
            }

        }

}


const val PAGE_SIZE_CLIENT = 30
const val PAGE_SIZE_CLIENT_REPO = 10



