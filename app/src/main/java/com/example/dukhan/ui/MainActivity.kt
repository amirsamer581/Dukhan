package com.example.dukhan.ui

import android.os.Bundle
import android.widget.SearchView
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.dukhan.databinding.ActivityMainBinding
import com.example.dukhan.domain.model.InventoryEntity
import com.example.dukhan.ui.adapter.ItemAdapter
import com.example.dukhan.ui.viewModel.ItemViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

/**
 * MainActivity is the main entry point of the application.
 * It displays a list of items and allows users to search and filter them.
 * This activity uses Hilt for dependency injection, View Binding for accessing views,
 * and a ViewModel to manage UI-related data.
 *
 * The list of items is displayed in a RecyclerView, and search functionality is provided
 * by a SearchView. The activity observes data changes from the ViewModel and updates the
 * UI accordingly.
 */
@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private val viewModel: ItemViewModel by viewModels()
    private lateinit var itemAdapter: ItemAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setOnRefreshListener()
        setupRecyclerView()
        setupSearch()
        setupObservers()
    }

    private fun setOnRefreshListener() {
        binding.apply {
            binding.refreshBtn.setOnClickListener {
                viewModel.refresh()
                searchView.setQuery("", false)
                searchView.clearFocus()
            }
            binding.swipeRefreshLayout.setOnRefreshListener {
                viewModel.refresh()
                searchView.setQuery("", false)
                searchView.clearFocus()
                swipeRefreshLayout.isRefreshing = false
            }
        }
    }

    private fun setupRecyclerView() {
        binding.itemRecyclerView.apply {
            layoutManager = LinearLayoutManager(this@MainActivity)
            addItemDecoration(
                DividerItemDecoration(this@MainActivity,
                    DividerItemDecoration.VERTICAL)
            )
        }
    }

    private fun setupSearch() {
        binding.searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                viewModel.setSearchQuery(newText ?: "")
                return true
            }
        })
    }

    private fun setupObservers() {
        lifecycleScope.launch(Dispatchers.Main) {
            viewModel.filteredItems
                .flowWithLifecycle(lifecycle, Lifecycle.State.STARTED)
                .collect { items ->
                    if (items.isNotEmpty()) {
                        updateRecycleView(items)
                    } else {
                        updateRecycleView(listOf(
                                InventoryEntity(
                                    itemNO = "No data found",
                                    name = "No data found",
                                    category = "No data found",
                                    qty = 0.0
                                )
                            )
                        )
                    }
                }
        }
    }

    private fun updateRecycleView(list : List<InventoryEntity>){
        itemAdapter = ItemAdapter(list)
        binding.itemRecyclerView.adapter = itemAdapter
    }
}