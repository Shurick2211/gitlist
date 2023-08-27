package com.nimko.gitlist.api.dto

import com.google.gson.annotations.SerializedName
import com.nimko.gitlist.dbservices.entitys.Client
import com.nimko.gitlist.dbservices.entitys.ClientRepo

data class SearchRepo(
    @SerializedName("total_count")val totalCount: Int,
    @SerializedName("incomplete_results") val incompleteResults: Boolean,
    val items: List<Repo>
){
    fun getSearchListOfRepo():List<ClientRepo> {
        return this.items.map {
            it.toEntity()
        }
    }
}
