package com.nimko.gitlist.storage.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState

typealias Loader<T> = suspend (perPage:Int, page:Int) -> List<T>

class PagingSource<T : Any>(
    val loader:Loader<T>,
    val perPage:Int
):PagingSource<Int,T>() {
    override fun getRefreshKey(state: PagingState<Int, T>): Int? {
        val anchorPosition = state.anchorPosition ?: return null
        val page = state.closestPageToPosition(anchorPosition) ?: return null
        return page.prevKey?.plus(1) ?: page.nextKey?.minus(1)
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, T> {
        val pageIndex = params.key ?: 0
        return try {
            val clients = loader.invoke(params.loadSize,pageIndex)
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