package com.example.dukhan.ui.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.dukhan.domain.useCase.GetItemsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
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

    init {
        refresh()
    }

    private val _searchQuery = MutableStateFlow("")
    fun setSearchQuery(query: String) {
        _searchQuery.value = query
    }

    fun refresh() {
        viewModelScope.launch {
            getItemsUseCase.lastSync()
        }
    }

    var allItems = getItemsUseCase.observeItemsUseCase()
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5000L),
                initialValue = emptyList()
            )

    val filteredItems = combine(
        allItems,
        _searchQuery
    ) { items, searchQuery ->
        if (searchQuery.isEmpty()) {
            items
        } else {
            items.filter { item ->
                item.name.contains(searchQuery, ignoreCase = true)
                        || item.category.contains(searchQuery, ignoreCase = true)
            }
        }
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000L),
        initialValue = emptyList()
    )
}