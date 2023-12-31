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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.SearchBar
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.paging.compose.collectAsLazyPagingItems
import coil.ImageLoader
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.nimko.gitlist.R
import com.nimko.gitlist.dbservices.entitys.Client
import com.nimko.gitlist.viewmodel.MyViewModel

@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("UnrememberedMutableState")
@Composable
fun ListClient (onClick: (param:String) -> Unit, model: MyViewModel){
    val defaultUsers = model.clientFlow
    val listFlow = remember {
        mutableStateOf(defaultUsers)
    }
    val searchBy = remember {
        mutableStateOf("")
    }
    val searchPanelSize = remember {
        mutableStateOf(0f)
    }

    Column(modifier = Modifier.fillMaxSize())
    {
        val users = listFlow.value.collectAsLazyPagingItems()
        TopAppBar(title =
        {
            Text(
            text = stringResource(id = R.string.clientscreen),

            fontSize = 25.sp,
        )
        },
            colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                containerColor = Color.Green.copy(alpha = 0.3f)
            ),
            actions = {
                SearchBar(
                    modifier = Modifier
                        .fillMaxWidth(searchPanelSize.value)
                        .padding(3.dp),
                    query = searchBy.value,
                    onQueryChange = {
                        searchBy.value = it
                        model.searchUserBy.value = it
                        if(it.isEmpty()) {
                            Log.d("ON_CHANGE","EMPTY")
                            listFlow.value = defaultUsers
                        }
                        else listFlow.value = model.getClientSearchPager()
                    },
                    onSearch = {
                        model.searchUserBy.value = it
                        Log.d("SEARCH",model.searchUserBy.value.toString())
                        listFlow.value = model.getClientSearchPager()
                    },
                    active = false,
                    onActiveChange = {},
                    placeholder = { Text(text = stringResource(id = R.string.search))}
                ) {}
                IconButton(
                    onClick = {
                            if(searchPanelSize.value < 0.1f) {
                                searchPanelSize.value = 0.6f
                            }
                            else {
                                searchPanelSize.value = 0F
                            }
                    }
                ) {
                    Icon(
                        Icons.Filled.Search,
                        contentDescription = stringResource(id = R.string.search),
                        modifier = Modifier.size(60.dp),
                        tint = Color.Black
                    )
                }
            }
        )

        LazyColumn(
            modifier = Modifier.fillMaxSize()
        ) {
            items(users.itemCount) {  item ->
                ClientListItems(onClick, users[item]!!)
            }
        }
    }
}

@Composable
fun ClientListItems(onClick: (param:String) -> Unit, item: Client) {
    val context = LocalContext.current
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
                },
            verticalAlignment = Alignment.CenterVertically
        ) {
            AsyncImage(
                modifier = Modifier
                    .size(100.dp)
                    .padding(5.dp)
                    .clip(CircleShape),
                model = ImageRequest.Builder(context = LocalContext.current)
                    .data(item.avatarUrl)
                    .crossfade(true)
                    .build(),
                imageLoader = ImageLoader(context),
                contentDescription = item.login,
                contentScale = ContentScale.Crop
            )
            Column {
                Text(
                    text = stringResource(id = R.string.login),
                    fontSize= 16.sp,
                    color = Color.Yellow,
                    modifier = Modifier
                        .padding(5.dp)
                )
                Text(
                    text = item.login,
                    fontSize= 30.sp,
                    color = Color.Yellow,
                    modifier = Modifier
                        .padding(5.dp)
                )
            }
        }
    }
}
