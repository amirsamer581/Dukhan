package com.example.dukhan.ui

import android.os.Bundle
import android.widget.SearchView
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.dukhan.databinding.ActivityMainBinding
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
    private var itemAdapter: ItemAdapter = ItemAdapter(emptyList())
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setupRecyclerView()
        setupSearch()
        setupObservers()
    }

    private fun setupRecyclerView(){
        binding.itemRecyclerView.apply {
            layoutManager = LinearLayoutManager(this@MainActivity)
            adapter = itemAdapter
        }
        binding.itemRecyclerView.addItemDecoration(
            DividerItemDecoration(this, DividerItemDecoration.VERTICAL)
        )
    }

    private fun setupSearch(){
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

    private fun setupObservers(){
        lifecycleScope.launch(Dispatchers.Main) {
            viewModel.itemStateFlowCombine
                .flowWithLifecycle(lifecycle, Lifecycle.State.STARTED)
                .collect { items ->
                    Toast.makeText(this@MainActivity, "Items fetched", Toast.LENGTH_LONG).show()
                    itemAdapter = ItemAdapter(items)
                    binding.itemRecyclerView.adapter = itemAdapter
                }
        }
    }
}