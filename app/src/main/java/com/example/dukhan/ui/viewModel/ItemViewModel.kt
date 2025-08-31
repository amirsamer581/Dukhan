package com.example.dukhan.ui.viewModel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.dukhan.constant.KeyConstant.ITEM_VIEWMODEL_TAG
import com.example.dukhan.domain.model.InventoryEntity
import com.example.dukhan.domain.useCase.GetItemsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

/**
 * ViewModel for managing and providing inventory item data to the UI.
 *
 * This ViewModel is responsible for:
 * - Fetching inventory items using the [GetItemsUseCase].
 * - Handling search functionality to filter items based on user input.
 * - Exposing the list of items as a [StateFlow] for reactive UI updates.
 * - Managing loading and error states for the item list.
 *
 * @property getItemsUseCase An instance of [GetItemsUseCase] used to retrieve item data.
 */
@HiltViewModel
class ItemViewModel @Inject constructor(
    private val getItemsUseCase: GetItemsUseCase
) : ViewModel() {

    private val _searchQuery = MutableStateFlow("")
    fun setSearchQuery(query: String) {
        _searchQuery.value = query
    }

    var itemStateFlow: StateFlow<List<InventoryEntity>> = flow {
        emitAll(getItemsUseCase.observeItemsUseCase())
    }.catch { exception ->
        Log.e(ITEM_VIEWMODEL_TAG, "Error in ViewModel Items flow", exception)
        emit(
            listOf(
                InventoryEntity(
                    itemNO = "Error $exception",
                    name = "Error",
                    category = "Error",
                    qty = 0.0
                )
            )
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000L),
        listOf(
            InventoryEntity(
                itemNO = "Loading...",
                name = "Loading...",
                category = "Loading...",
                qty = 0.0
            )
        )
    )

    val itemStateFlowCombine: StateFlow<List<InventoryEntity>> = combine(
        itemStateFlow,
        _searchQuery
    ) { items, searchQuery ->
        if (searchQuery.isEmpty()) {
            items
        } else {
            items.filter { item ->
                item.name.contains(searchQuery, ignoreCase = true)
            }
        }
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000L),
        initialValue = listOf(
            InventoryEntity(
                itemNO = "Loading...",
                name = "Loading...",
                category = "Loading...",
                qty = 0.0
            )
        )
    )
}