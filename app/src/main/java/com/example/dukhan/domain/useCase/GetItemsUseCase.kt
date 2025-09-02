package com.example.dukhan.domain.useCase

import com.example.dukhan.domain.model.InventoryEntity
import com.example.dukhan.domain.repository.ItemRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

/**
 * Use case for retrieving a list of inventory items.
 *
 * This class encapsulates the business logic for fetching items from the repository.
 * It provides a suspend function to observe a Flow of item lists, allowing for reactive updates.
 *
 * @property itemRepository The repository responsible for data access.
 */
class GetItemsUseCase @Inject constructor(
    private val itemRepository: ItemRepository) {

    fun observeItemsUseCase() : Flow<List<InventoryEntity>>{
        return itemRepository.getItems()
    }
    suspend fun lastSync() {
        itemRepository.lastSync()
    }
}