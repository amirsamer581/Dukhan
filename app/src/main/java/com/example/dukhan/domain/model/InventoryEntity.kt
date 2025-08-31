package com.example.dukhan.domain.model

import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "inventory")
data class InventoryEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,

    val itemNO : String,

    val name : String,

    val category : String,

    val qty : Double
)
