package com.example.allmyfriends.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.LinearLayout
import androidx.activity.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.paging.LoadState
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.allmyfriends.databinding.ActivityMainBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private val viewModel by viewModels<MainActivityViewModel>()
    private lateinit var binding: ActivityMainBinding
    private lateinit var adapter: PersonListAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        adapter = PersonListAdapter()
        adapter.stateRestorationPolicy = RecyclerView.Adapter.StateRestorationPolicy.ALLOW
        val linearLayoutManager = LinearLayoutManager(this@MainActivity)
        with(binding) {
            recyclerView.layoutManager = linearLayoutManager
            recyclerView.adapter = adapter
        }

        if (savedInstanceState != null) {
            val notLoading = adapter.loadStateFlow
                .distinctUntilChangedBy { it.refresh }
                // Only react to cases where Remote REFRESH completes i.e., NotLoading.
                .map { it.refresh is LoadState.NotLoading }

            val shouldScrollToTop = notLoading.distinctUntilChanged()
            val pagingData = viewModel.pagingData

            lifecycleScope.launch {
                combine(shouldScrollToTop, pagingData, ::Pair)
                    .distinctUntilChangedBy { it.second }
                    .collectLatest { (shouldScroll, pagingData) ->
                        adapter.submitData(pagingData)
                        // Scroll only after the data has been submitted to the adapter
                        if (shouldScroll) linearLayoutManager.scrollToPosition(0)
                    }
            }
        } else
            lifecycleScope.launchWhenCreated {
                viewModel.getUsers().collectLatest {
                            adapter.submitData(it)
                }
            }
    }

    companion object {
        const val TAG = "MainActivity"
    }
}