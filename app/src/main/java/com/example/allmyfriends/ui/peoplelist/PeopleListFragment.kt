package com.example.allmyfriends.ui.peoplelist

import android.opengl.Visibility
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.paging.LoadState
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.allmyfriends.databinding.FragmentPeopleListBinding
import com.example.allmyfriends.model.Person
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.distinctUntilChanged
import ru.beryukhov.reactivenetwork.ReactiveNetwork

@AndroidEntryPoint
class PeopleListFragment : Fragment(), PeopleListAdapter.OnPersonClickListener {
    private val viewModel by viewModels<PeopleListViewModel>()
    private var _binding: FragmentPeopleListBinding? = null
    private val binding get() = _binding!!
    private lateinit var adapter: PeopleListAdapter
    private lateinit var layoutManager: GridLayoutManager
    private lateinit var snackbar: Snackbar

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPeopleListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observeConnectivity()
        setupRecyclerView()
        displayData()
    }

    private fun observeConnectivity() {
        snackbar = Snackbar.make(binding.root, "Offline mode", Snackbar.LENGTH_INDEFINITE)
            .setAction("Dismiss") { snackbar.dismiss() }
        lifecycleScope.launchWhenCreated {
            ReactiveNetwork().observeNetworkConnectivity(requireContext()).collectLatest {
                if (!it.available)
                    snackbar.show()
                if (it.available)
                    snackbar.dismiss()
                viewModel.isInternetAvailable.emit(it.available)
            }
        }
    }

    private fun setupRecyclerView() {
        layoutManager = GridLayoutManager(requireContext(), 2)
        adapter = PeopleListAdapter(this)
        with(binding) {
            recyclerView.layoutManager = layoutManager
            recyclerView.adapter = adapter
            swipeRefresh.setOnRefreshListener {
                if (viewModel.isInternetAvailable.value) {
                    adapter.refresh()
                } else
                    Toast.makeText(
                        requireContext(), "Please connect to the internet to refresh the page",
                        Toast.LENGTH_LONG
                    ).show()
                swipeRefresh.isRefreshing = false
            }
        }
    }

    private fun displayData() {
        lifecycleScope.launch {
            viewModel.pagingData.collect { pagingData ->
                adapter.submitData(pagingData)
            }
        }

        lifecycleScope.launch {
            adapter.loadStateFlow.collect { loadState ->
                // Show loading spinner during initial load or refresh.
                binding.progressBar.isVisible = loadState.source.refresh is LoadState.Loading
            }
        }
    }

    override fun onPersonClick(person: Person) {
        findNavController()
            .navigate(
                PeopleListFragmentDirections
                    .actionPeopleListFragmentToPersonDetailFragment(person = person)
            )
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding.swipeRefresh.removeAllViews()
        _binding = null
    }

    companion object {
        private const val TAG = "PeopleListFragment"
    }
}