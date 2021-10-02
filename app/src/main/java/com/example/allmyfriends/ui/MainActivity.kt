package com.example.allmyfriends.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.LinearLayout
import android.widget.Toast
import androidx.activity.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.paging.LoadState
import androidx.paging.LoadType
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.allmyfriends.databinding.ActivityMainBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.flow.distinctUntilChanged
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
        adapter.stateRestorationPolicy = RecyclerView.Adapter.StateRestorationPolicy.PREVENT_WHEN_EMPTY
        with(binding){
            recyclerView.layoutManager = GridLayoutManager(this@MainActivity, 2)
            recyclerView.adapter = adapter
        }
    }

    override fun onStart() {
        super.onStart()
        viewModel.getUsers().observe(this) {
            if (it.data != null) {
                lifecycleScope.launchWhenStarted {
                    adapter.submitData(it.data)
                }
            }
        }
    }

    companion object{
        const val TAG = "MainActivity"
    }
}