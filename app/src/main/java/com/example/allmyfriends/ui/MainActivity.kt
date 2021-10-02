package com.example.allmyfriends.ui

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.AttributeSet
import android.util.Log
import android.view.View
import androidx.activity.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.allmyfriends.databinding.ActivityMainBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private val viewModel by viewModels<MainActivityViewModel>()
    private lateinit var binding: ActivityMainBinding
    private lateinit var adapter: PersonListAdapter
    private var layoutManager = LinearLayoutManager(this@MainActivity)
    private val visibleThreshold = 5
    private var isLoading = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        adapter = PersonListAdapter()
        adapter.stateRestorationPolicy =
            RecyclerView.Adapter.StateRestorationPolicy.PREVENT_WHEN_EMPTY
        with(binding) {
            recyclerView.layoutManager = layoutManager
            recyclerView.adapter = adapter
        }
        initScrollListener()
    }

    override fun onStart() {
        super.onStart()
        observeUsers()
    }

    private fun observeUsers(page: Int = 1) {
        viewModel.getUsers(page).observe(this) {
            if (it.data != null) {
                Log.d(TAG, "onStart: list = ${it.data}")
                lifecycleScope.launchWhenStarted {
                    adapter.submitList(it.data)
                }
            }
        }
    }

    private fun initScrollListener() {
        val scrollListener: RecyclerView.OnScrollListener =
            object : RecyclerView.OnScrollListener() {
                override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                    super.onScrolled(recyclerView, dx, dy)
                    val lastVisibleItem: Int =
                        layoutManager.findLastCompletelyVisibleItemPosition()
                    if (!isLoading && lastVisibleItem == adapter.currentList.size - visibleThreshold) {
                        loadNextDataFromApi()
                        isLoading = true
                    }
                }
            }
        binding.recyclerView.addOnScrollListener(scrollListener)
    }

    private fun loadNextDataFromApi() {
        val handler = Handler(Looper.getMainLooper())
        handler.postDelayed({
           observeUsers(adapter.currentList.last().pageKey)
        }, 2000)
    }


    companion object {
        const val TAG = "MainActivity"
    }
}