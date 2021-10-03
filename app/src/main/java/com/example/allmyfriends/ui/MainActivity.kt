package com.example.allmyfriends.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.paging.LoadState
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.allmyfriends.databinding.ActivityMainBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import ru.beryukhov.reactivenetwork.ReactiveNetwork

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private val viewModel by viewModels<MainActivityViewModel>()
    private lateinit var binding: ActivityMainBinding
    private lateinit var adapter: PersonListAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        observeConnectivity()
        setupRecyclerView()
    }

    private fun observeConnectivity() {
        lifecycleScope.launchWhenStarted {
            ReactiveNetwork().observeNetworkConnectivity(this@MainActivity).collectLatest {
                viewModel.isInternetAvailable.emit(it.available)
            }
        }
    }

    private fun setupRecyclerView() {
        val linearLayoutManager = LinearLayoutManager(this@MainActivity)
        adapter = PersonListAdapter()
        //adapter.stateRestorationPolicy = RecyclerView.Adapter.StateRestorationPolicy.ALLOW
        with(binding) {
            recyclerView.layoutManager = linearLayoutManager
            recyclerView.adapter = adapter
            swipeRefresh.setOnRefreshListener {
                adapter.refresh()
                swipeRefresh.isRefreshing = false
            }
        }
        displayData(linearLayoutManager)
    }

    private fun displayData(linearLayoutManager: LinearLayoutManager) {
        val notLoading = adapter.loadStateFlow
            .distinctUntilChangedBy { it.refresh }
            // Only react to cases where Remote REFRESH completes i.e., NotLoading.
            .map { it.refresh is LoadState.NotLoading }

        val shouldScrollToTop = notLoading.distinctUntilChanged()

        lifecycleScope.launch {
            combine(shouldScrollToTop, viewModel.pagingData, ::Pair)
                .distinctUntilChangedBy { it.second }
                .collectLatest { (shouldScroll, pagingData) ->
                    adapter.submitData(pagingData)
                    // Scroll only after the data has been submitted to the adapter
                    if (shouldScroll) linearLayoutManager.scrollToPosition(0)
                }
        }
    }

    companion object {
        const val TAG = "MainActivity"
    }
}