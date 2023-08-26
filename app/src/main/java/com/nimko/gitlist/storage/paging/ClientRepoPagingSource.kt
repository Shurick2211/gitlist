package com.nimko.gitlist.storage.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.nimko.gitlist.dbservices.entitys.ClientRepo
import com.nimko.gitlist.storage.Storage


class ClientRepoPagingSource(
    val storage: Storage,
    val perPage:Int
):PagingSource<Int,ClientRepo>() {
    override fun getRefreshKey(state: PagingState<Int, ClientRepo>): Int? {
        val anchorPosition = state.anchorPosition ?: return null
        val page = state.closestPageToPosition(anchorPosition) ?: return null
        return page.prevKey?.plus(1) ?: page.nextKey?.minus(1)
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, ClientRepo> {
        val pageIndex = params.key ?: 0
        return try {
            val clients = storage.getClientRepo(params.loadSize, pageIndex)
            return LoadResult.Page(
                data = clients,
                prevKey = if(pageIndex == 0) null else pageIndex-1,
                nextKey = if(params.loadSize == clients.size) pageIndex + (params.loadSize/perPage)
                else null
            )
        } catch (e:Exception){
            LoadResult.Error(
                throwable = e
            )
        }
    }
}