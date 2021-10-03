package com.example.allmyfriends.ui.peoplelist

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.paging.LoadState
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.allmyfriends.databinding.FragmentPeopleListBinding
import com.example.allmyfriends.model.Person
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import ru.beryukhov.reactivenetwork.ReactiveNetwork

@AndroidEntryPoint
class PeopleListFragment: Fragment(), PeopleListAdapter.OnPersonClickListener {
    private val viewModel by viewModels<PeopleListViewModel>()
    private var _binding: FragmentPeopleListBinding? = null
    private val binding get() = _binding!!
    private lateinit var adapter: PeopleListAdapter


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
    }

    private fun observeConnectivity() {
        lifecycleScope.launchWhenCreated {
            ReactiveNetwork().observeNetworkConnectivity(requireContext()).collectLatest {
                viewModel.isInternetAvailable.emit(it.available)
            }
        }
    }

    private fun setupRecyclerView() {
        val linearLayoutManager = GridLayoutManager(requireContext(), 2)
        adapter = PeopleListAdapter(this)
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

        lifecycleScope.launchWhenCreated {
            combine(shouldScrollToTop, viewModel.pagingData, ::Pair)
                .distinctUntilChangedBy { it.second }
                .collectLatest { (shouldScroll, pagingData) ->
                    adapter.submitData(pagingData)
                    // Scroll only after the data has been submitted to the adapter
                    if (shouldScroll) linearLayoutManager.scrollToPosition(0)
                }
        }
    }

    override fun onPersonClick(person: Person) {
        findNavController()
            .navigate(PeopleListFragmentDirections
                .actionPeopleListFragmentToPersonDetailFragment(person = person))
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