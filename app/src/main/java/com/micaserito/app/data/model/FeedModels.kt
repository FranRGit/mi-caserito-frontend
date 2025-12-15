package com.micaserito.app.data.model

import com.google.gson.annotations.SerializedName

data class HomeFeedResponse(
    @SerializedName("status") val status: String,
    @SerializedName("data") val data: FeedData,
    @SerializedName("pagination") val pagination: PaginationMeta
)

data class FeedData(
    @SerializedName("sections") val sections: List<FeedSection>? = null,
    @SerializedName("items") val items: List<FeedItem>? = null
)

data class FeedSection(
    @SerializedName("type") val type: String,
    @SerializedName("title") val title: String,
    @SerializedName("items") val items: List<FeedItem>
)

data class PaginationMeta(
    @SerializedName("current_page") val currentPage: Int,
    @SerializedName("total_pages") val totalPages: Int,
    @SerializedName("has_more") val hasMore: Boolean
)

data class FeedItem(
    @SerializedName("type") val type: String,
    @SerializedName("details") val details: ItemDetails
)
