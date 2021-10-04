package com.example.allmyfriends.ui.peoplelist

import android.os.Bundle
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
import com.example.allmyfriends.R
import com.example.allmyfriends.databinding.FragmentPeopleListBinding
import com.example.allmyfriends.model.Person
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
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
        snackbar = Snackbar.make(
            binding.root,
            getString(R.string.offline_mode),
            Snackbar.LENGTH_INDEFINITE
        )
            .setAction(getString(R.string.dismiss)) { snackbar.dismiss() }
       lifecycleScope.launchWhenCreated {
            ReactiveNetwork().observeNetworkConnectivity(requireContext()).collect {
                if (!it.available)
                    snackbar.show()
                if (it.available)
                    snackbar.dismiss()
                 viewModel.isInternetAvailable.emit(it.available)
            }
        }
    }

    private fun setupRecyclerView() {
        layoutManager = GridLayoutManager(requireContext(), SPAN_COUNT)
        adapter = PeopleListAdapter(this)
        with(binding) {
            recyclerView.layoutManager = layoutManager
            recyclerView.adapter = adapter
            swipeRefresh.setOnRefreshListener {
                if (viewModel.isInternetAvailable.value) {
                    adapter.refresh()
                } else
                    Toast.makeText(
                        requireContext(), getString(R.string.reconnect_to_internet),
                        Toast.LENGTH_LONG
                    ).show()
                swipeRefresh.isRefreshing = false
            }
        }
    }


    /*
    * More informations:
    * https://github.com/googlecodelabs/android-paging/issues/100#issuecomment-872347614
    */
    private fun displayData() {
        val notLoading = adapter.loadStateFlow
            .distinctUntilChangedBy { it.refresh }
            // Only react to cases where REFRESH completes i.e., NotLoading.
            .map { it.refresh is LoadState.NotLoading }

        val shouldScrollToTheTop = notLoading.distinctUntilChanged()

        lifecycleScope.launchWhenStarted {
            combine(shouldScrollToTheTop, viewModel.pagingData, ::Pair)
                .distinctUntilChangedBy { it.second }
                .collect { (canScroll, pagingData) ->
                    adapter.submitData(pagingData)
                    if (canScroll) layoutManager.scrollToPosition(0)
                }
        }

        lifecycleScope.launchWhenStarted {
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
        private const val SPAN_COUNT = 2

    }
}