package com.example.dukhan.data.repository

import android.util.Log
import com.example.dukhan.constant.KeyConstant.ITEM_REPOSITORY_IMPL
import com.example.dukhan.data.local.ItemDao
import com.example.dukhan.data.remote.ItemBalanceApiService
import com.example.dukhan.data.remote.ItemMasterApiService
import com.example.dukhan.domain.model.InventoryEntity
import com.example.dukhan.domain.model.ItemBalance
import com.example.dukhan.domain.model.ItemMaster
import com.example.dukhan.domain.repository.ItemRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import javax.inject.Inject

/**
 * Implementation of the [ItemRepository] interface.
 * This class is responsible for fetching item data from either a local database or a remote API.
 * It uses Dagger Hilt for dependency injection.
 *
 * @property itemMasterApiService Service for fetching item master data from the remote API.
 * @property itemBalanceApiService Service for fetching item balance data from the remote API.
 * @property itemDao Data Access Object for interacting with the local item database.
 */
class ItemRepositoryImpl @Inject constructor(
    private val itemMasterApiService: ItemMasterApiService,
    private val itemBalanceApiService: ItemBalanceApiService,
    private val itemDao: ItemDao
) : ItemRepository {

    private val localItems by lazy {
        itemDao.getAllItems()
    }

    override fun getItems(): Flow<List<InventoryEntity>> {
        return localItems.map { items ->
                items.map { entity ->
                    InventoryEntity(
                        itemNO = entity.itemNO,
                        name = entity.name,
                        category = entity.category,
                        qty = entity.qty
                    )
                }
            }
            .flowOn(Dispatchers.IO)
    }

    override suspend fun lastSync() {
        try {
            Log.d(ITEM_REPOSITORY_IMPL, "Refetching from remote...")
            val itemMaster = itemMasterApiService.getItemMaster().itemsMaster
            val itemBalance = itemBalanceApiService.getItemBalance().salesManItemsBalance
            val mergedData = mergeData(itemMaster, itemBalance)
            if (localItems.first().isNotEmpty()) {
                itemDao.refreshItems(mergedData)
                Log.d(ITEM_REPOSITORY_IMPL, "Items cleared and inserted into local database")
            } else {
                itemDao.insertALLItems(mergedData)
                Log.d(ITEM_REPOSITORY_IMPL, "Items inserted into local database")
            }
        } catch (e: Exception) {
            Log.e(ITEM_REPOSITORY_IMPL, "Error fetching items from remote", e)
        }
    }

    private fun mergeData(
        itemMaster: List<ItemMaster>,
        itemBalance: List<ItemBalance>
    ): List<InventoryEntity> {
        return itemMaster.map { item ->
            val balance = itemBalance.find { it.itemOCode == item.itemNO }
            InventoryEntity(
                itemNO = item.itemNO,
                name = item.name,
                category = item.category,
                qty = balance?.qty ?: 0.0
            )
        }
    }
}