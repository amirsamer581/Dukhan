package com.example.dukhan.data.remote

import com.example.dukhan.domain.model.ItemBalanceResponse
import retrofit2.http.GET
import retrofit2.http.Query


/**
 * Interface defining API endpoints for retrieving item balance information.
 *
 * This interface uses Retrofit annotations to specify the HTTP request method,
 * endpoint path, and query parameters for API calls related to item balances.
 */
interface ItemBalanceApiService {

    @GET("getvanalldata")
    suspend fun getItemBalance(
        @Query("cono")
        cono : Int = 290,
        @Query("strno")
        strno : Int = 1,
        @Query("case")
        case : Int = 9
    ) : ItemBalanceResponse
}