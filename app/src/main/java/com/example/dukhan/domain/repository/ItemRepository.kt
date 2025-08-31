package com.example.dukhan.domain.repository

import com.example.dukhan.domain.model.InventoryEntity
import kotlinx.coroutines.flow.Flow

interface ItemRepository {
    suspend fun getItems() : Flow<List<InventoryEntity>>
}