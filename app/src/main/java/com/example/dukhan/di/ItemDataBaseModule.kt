package com.example.dukhan.di

import android.content.Context
import com.example.dukhan.constant.KeyConstant.ITEM_BASE_URL
import com.example.dukhan.data.local.ItemDataBase
import com.example.dukhan.data.remote.ItemBalanceApiService
import com.example.dukhan.data.remote.ItemMasterApiService
import com.example.dukhan.data.repository.ItemRepositoryImpl
import com.example.dukhan.domain.repository.ItemRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

/**
 * Dagger Hilt module for providing dependencies related to item data.
 * This module is installed in the [SingletonComponent], meaning the provided dependencies
 * will have a singleton scope and live as long as the application.
 */
@Module
@InstallIn(SingletonComponent::class)
object ItemDataBaseModule {
    @Provides
    @Singleton
    fun provideRetrofit() : Retrofit {
        return Retrofit.Builder()
            .baseUrl(ITEM_BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    @Provides
    @Singleton
    fun provideItemMasterApiService(retrofit: Retrofit) : ItemMasterApiService {
        return retrofit.create(ItemMasterApiService::class.java)
    }

    @Provides
    @Singleton
    fun provideItemBalanceApiService(retrofit: Retrofit) : ItemBalanceApiService {
        return retrofit.create(ItemBalanceApiService::class.java)
    }

    @Provides
    @Singleton
    fun provideItemDataBase(@ApplicationContext context: Context) : ItemDataBase{
        return ItemDataBase.getDatabase(context)
    }

    @Provides
    @Singleton
    fun provideItemRepository(
        itemMasterApiService : ItemMasterApiService,
        itemBalanceApiService : ItemBalanceApiService,
        itemDataBase: ItemDataBase
    ) : ItemRepository {
        return ItemRepositoryImpl(
            itemMasterApiService,
            itemBalanceApiService,
            itemDataBase.itemDao()
        )
    }
}