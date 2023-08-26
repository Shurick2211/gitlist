package com.nimko.gitlist.ui.screens

import android.annotation.SuppressLint
import android.view.ViewGroup
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.KeyboardArrowLeft
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import com.nimko.gitlist.viewmodel.MyViewModel


@SuppressLint("SetJavaScriptEnabled")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WebScreen(onClick: () -> Unit, model: MyViewModel){
    val url:String = model.url.value!!
    val uu = url.substring(url.lastIndexOf("/"))
    var webView: WebView? = null
    Column(modifier = Modifier.fillMaxSize())
    {
        TopAppBar(title = {
            Row {
                Text(
                    text = "$uu:" ,
                    color = Color.Red,
                    fontSize = 16.sp,
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
                        Icons.Filled.ArrowBack,
                        contentDescription = "Back",
                        modifier = Modifier.size(60.dp),
                        tint = Color.Black
                    )
                }
            },
            actions = {
                IconButton(
                    onClick = {
                        webView!!.goBack()
                    }
                ) {
                    Icon(
                        Icons.Filled.KeyboardArrowLeft,
                        contentDescription = "Back",
                        modifier = Modifier.size(80.dp),
                        tint = Color.Black
                    )
                }
                IconButton(
                    onClick = {
                        webView!!.goForward()
                    }
                ) {
                    Icon(
                        Icons.Filled.KeyboardArrowRight,
                        contentDescription = "Forward",
                        modifier = Modifier.size(80.dp),
                        tint = Color.Black
                    )
                }
            },
            colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                containerColor = Color.Green.copy(alpha = 0.3f)
            )
        )
        AndroidView(modifier = Modifier.fillMaxSize(),
            factory = {
                WebView(it).apply {
                    layoutParams = ViewGroup.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.MATCH_PARENT
                    )
                    loadUrl(url)
                    webViewClient = WebViewClient()
                    settings.javaScriptEnabled = true
                }
            }, update = {
                it.loadUrl(url)
                webView = it
            })
    }
}
