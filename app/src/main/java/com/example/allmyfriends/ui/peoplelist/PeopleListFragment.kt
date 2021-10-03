package com.example.allmyfriends.ui.peoplelist

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
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.launch
import ru.beryukhov.reactivenetwork.ReactiveNetwork

@AndroidEntryPoint
class PeopleListFragment : Fragment(), PeopleListAdapter.OnPersonClickListener {
    private val viewModel by viewModels<PeopleListViewModel>()
    private var _binding: FragmentPeopleListBinding? = null
    private val binding get() = _binding!!
    private lateinit var adapter: PeopleListAdapter
    private lateinit var layoutManager: GridLayoutManager


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

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
        lifecycleScope.launchWhenStarted {
            ReactiveNetwork().observeNetworkConnectivity(requireContext()).collectLatest {
                val snackbar =
                    Snackbar.make(binding.root, "Offline mode", Snackbar.LENGTH_INDEFINITE)
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
            recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
                override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                    super.onScrolled(recyclerView, dx, dy)
                    viewModel.scrollPosition = layoutManager.findFirstVisibleItemPosition()
                }
            })
            swipeRefresh.setOnRefreshListener {
                adapter.refresh()
                swipeRefresh.isRefreshing = false
            }
        }
    }

    private fun displayData() {
        lifecycleScope.launch {
            viewModel.pagingData.collectLatest { pagingData ->
                adapter.submitData(pagingData)
                layoutManager.scrollToPosition(viewModel.scrollPosition)
            }
        }

        lifecycleScope.launch {
            adapter.loadStateFlow.collect { loadState ->
                // Show loading spinner during initial load or refresh.
                binding.progressBar.isVisible = loadState.source.refresh is LoadState.Loading
                val errorState = loadState.source.append as? LoadState.Error
                    ?: loadState.source.prepend as? LoadState.Error
                    ?: loadState.append as? LoadState.Error
                    ?: loadState.prepend as? LoadState.Error
                errorState?.let {
                    Toast.makeText(
                        requireContext(), "\uD83D\uDE28 Wooops ${it.error}",
                        Toast.LENGTH_LONG).show()
                }
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