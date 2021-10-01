package com.example.allmyfriends.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.LinearLayout
import androidx.activity.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.allmyfriends.databinding.ActivityMainBinding
import dagger.hilt.android.AndroidEntryPoint
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
        binding.recyclerView.layoutManager = LinearLayoutManager(this)
        binding.recyclerView.adapter = adapter
    }

    override fun onStart() {
        super.onStart()
        viewModel.getUsers().observe(this) {
            if (it.data != null)
                lifecycleScope.launchWhenStarted {
                    adapter.submitData(it.data)
                    Log.d(TAG, "onStart: submitdata")
                }
                Log.d(TAG, "onCreate: data = ${it.error?.message}")
        }

    }

    companion object{
        const val TAG = "MainActivity"
    }
}