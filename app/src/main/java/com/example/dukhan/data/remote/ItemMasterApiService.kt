package com.example.dukhan.data.remote

import com.example.dukhan.domain.model.ItemMasterResponse
import retrofit2.http.GET
import retrofit2.http.Query


/**
 * Interface defining the API endpoints for fetching item master data.
 * This interface is used by Retrofit to generate the network request implementations.
 */
interface ItemMasterApiService {

    @GET("getvanalldata")
    suspend fun getItemMaster(
        @Query("cono")
        cono : Int = 290,
        @Query("strno")
        strno : Int = 1,
        @Query("case")
        case : Int = 4
    ) : ItemMasterResponse
}