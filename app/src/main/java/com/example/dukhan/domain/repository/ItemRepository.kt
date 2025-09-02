package com.example.dukhan.domain.repository

import com.example.dukhan.domain.model.InventoryEntity
import kotlinx.coroutines.flow.Flow

interface ItemRepository {
    fun getItems() : Flow<List<InventoryEntity>>

    suspend fun lastSync()
}