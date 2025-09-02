package com.example.dukhan.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.example.dukhan.domain.model.InventoryEntity
import kotlinx.coroutines.flow.Flow

/**
 * Data Access Object (DAO) for the inventory table.
 * Provides methods for interacting with the inventory data in the database.
 */
@Dao
interface ItemDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertALLItems(item: List<InventoryEntity>)

    @Query("SELECT * FROM inventory")
    fun getAllItems(): Flow<List<InventoryEntity>>

    @Query("DELETE FROM inventory")
    suspend fun clearAllItems()

    @Transaction
    suspend fun refreshItems(item: List<InventoryEntity>) {
        clearAllItems()
        insertALLItems(item)
    }
}