package com.example.dukhan.domain.model

import com.google.gson.annotations.SerializedName

data class ItemBalanceResponse(
    @SerializedName("SalesMan_Items_Balance")
    val salesManItemsBalance: List<ItemBalance>
)
