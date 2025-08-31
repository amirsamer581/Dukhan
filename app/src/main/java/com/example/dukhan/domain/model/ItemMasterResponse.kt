package com.example.dukhan.domain.model

import com.google.gson.annotations.SerializedName

data class ItemMasterResponse(
    @SerializedName("Items_Master")
    val itemsMaster: List<ItemMaster>
)