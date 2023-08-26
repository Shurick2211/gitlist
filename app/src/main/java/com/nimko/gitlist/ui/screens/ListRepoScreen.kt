package com.nimko.gitlist.ui.screens

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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.paging.compose.collectAsLazyPagingItems
import com.nimko.gitlist.R
import com.nimko.gitlist.dbservices.entitys.ClientRepo
import com.nimko.gitlist.ui.theme.Pink80
import com.nimko.gitlist.viewmodel.MyViewModel

@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("UnrememberedMutableState")
@Composable
fun ListClientRepos (onClick: (event:Int) -> Unit, model: MyViewModel){
    val login = model.login.value!!
    val listRepo = model.getClientRepoPager().collectAsLazyPagingItems()
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
                    onClick(0)
                }
            ) {
                Icon(
                    Icons.Filled.ArrowBack,
                    contentDescription = "Back",
                    modifier = Modifier.size(60.dp),
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
                 ClientRepoListItems(onClick, listRepo[item]!!, model)
            }
        }
    }
}

@Composable
fun ClientRepoListItems(onClick: (event:Int) -> Unit, item: ClientRepo, model:MyViewModel) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(1.dp)
            .clickable {
                Log.d("My Log", "Click ${item.id} ${item.name} ${item.url}")
                model.url.postValue(item.url)
                onClick(1)
            }
        ,
        shape = RoundedCornerShape(15.dp)
    ) {
            Column(Modifier
                        .background(color = Pink80),) {
                Row(
                    Modifier
                        .fillMaxWidth()
                        .padding(start = 3.dp),
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
                        text = item.name,
                        fontSize= 30.sp,
                        color = Color.Black,
                        modifier = Modifier
                            .padding(start = 3.dp),
                    )
                }
                Row(
                    Modifier
                        .fillMaxWidth()
                        .padding(start = 3.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = stringResource(id = R.string.id),
                        fontSize= 16.sp,
                        color = Color.Black,
                        modifier = Modifier
                            .padding(start = 3.dp)
                    )
                    Text(
                        text = item.language ?: "—",
                        fontSize= 24.sp,
                        color = Color.Black,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(start = 3.dp)
                    )
                }

            }

        }

}
