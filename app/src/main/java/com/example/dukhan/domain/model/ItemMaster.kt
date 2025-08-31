package com.example.dukhan.domain.model

import com.google.gson.annotations.SerializedName


data class ItemMaster(
    @SerializedName("ITEMNO")
    val itemNO : String,
    @SerializedName("NAME")
    val name : String,
    @SerializedName("CATEOGRYID")
    val category : String,
)
