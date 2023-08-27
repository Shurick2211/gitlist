package com.nimko.gitlist.api.dto

import com.google.gson.annotations.SerializedName
import com.nimko.gitlist.dbservices.entitys.Client

data class SearchUser(
    @SerializedName("total_count")val totalCount: Int,
    @SerializedName("incomplete_results") val incompleteResults: Boolean,
    val items: List<Person>
){
    fun getSearchListOfClient():List<Client> {
        return this.items.map {
            it.toEntity()
        }
    }
}
