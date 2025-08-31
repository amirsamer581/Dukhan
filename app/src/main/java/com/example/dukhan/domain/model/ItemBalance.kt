package com.example.dukhan.domain.model

import com.google.gson.annotations.SerializedName

data class ItemBalance(
    @SerializedName("ItemOCode")
    val itemOCode : String,
    @SerializedName("QTY")
    val qty : Double
)
