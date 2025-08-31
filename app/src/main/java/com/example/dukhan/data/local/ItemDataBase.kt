package com.example.dukhan.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.dukhan.domain.model.InventoryEntity

/**
 * Room Database for managing inventory items.
 *
 * This class provides access to the underlying SQLite database using Room persistence library.
 * It defines the database configuration, entities, and DAOs.
 *
 * @property itemDao Provides access to the Data Access Object (DAO) for inventory items.
 * @constructor Creates an instance of ItemDataBase.
 */
@Database(entities = [InventoryEntity::class], version = 2, exportSchema = false)
abstract class ItemDataBase : RoomDatabase() {

    abstract fun itemDao(): ItemDao

    companion object {
        @Volatile
        var INSTANCE: ItemDataBase? = null

        fun getDatabase(context: Context): ItemDataBase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    ItemDataBase::class.java,
                    "item_database"
                )
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}