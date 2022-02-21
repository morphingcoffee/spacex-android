package com.morphingcoffee.spacex.presentation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import com.morphingcoffee.spacex.R
import com.morphingcoffee.spacex.databinding.FragmentMainBinding
import com.morphingcoffee.spacex.domain.model.Launch
import com.morphingcoffee.spacex.presentation.recyclerview.LaunchesAdapter
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.sharedViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel


/**
 * Main screen of the app where the user sees the Company info and the list of launches.
 *
 * If the user selects filtering action on the app bar they will be directed to another (dialog)
 * fragment, so the sorting & filtering is handled there.
 **/
class CompanyLaunchesFragment : Fragment() {
    private lateinit var binding: FragmentMainBinding
    private val adapter: LaunchesAdapter by inject()

    private val companyViewModel: CompanyViewModel by viewModel()
    private val launchesViewModel: LaunchesViewModel by sharedViewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        adapter.setOnLaunchSelectedListener { onLaunchClicked(it) }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentMainBinding.inflate(inflater, container, false)
        onBindingReady()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpObservers()
    }

    private fun onBindingReady() {
        with(binding) {
            recyclerView.apply {
                adapter = this@CompanyLaunchesFragment.adapter
                addItemDecoration(
                    DividerItemDecoration(
                        requireContext(),
                        DividerItemDecoration.VERTICAL
                    )
                )
            }
            swipeRefresh.setOnRefreshListener { requestDataRefresh() }
        }
    }

    private fun setUpObservers() {
        companyViewModel.state.observe(viewLifecycleOwner) { state ->
            when (state) {
                is CompanyViewModel.UiState.Display -> displayCompanyData(state)
                is CompanyViewModel.UiState.Error -> displayError(state.errorRes)
                is CompanyViewModel.UiState.Loading -> displayProgress()
            }
        }
        launchesViewModel.uiState.observe(viewLifecycleOwner) { state ->
            when (state) {
                is LaunchesViewModel.UiState.Display -> displayLaunchesData(state)
                is LaunchesViewModel.UiState.Error -> displayError(state.errorRes)
                LaunchesViewModel.UiState.Loading -> displayProgress()
            }
        }
    }

    private fun onLaunchClicked(launch: Launch) {
        val links = launch.links
        val action = CompanyLaunchesFragmentDirections.actionMainFragmentToDialogOpenLinks(
            links?.webcastURL,
            links?.wikiURL,
            links?.articleURL
        )
        findNavController().navigate(action)
    }

    private fun requestDataRefresh() {
        launchesViewModel.handleUserAction(LaunchesViewModel.UserAction.Refresh)
        companyViewModel.handleUserAction(CompanyViewModel.UserAction.Refresh)
    }

    private fun displayCompanyData(state: CompanyViewModel.UiState.Display) {
        val company = state.company
        binding.companyDescription.text = getString(
            R.string.company_description_template,
            company.companyName,
            company.founderName,
            company.foundedYear,
            company.numOfEmployees,
            company.launchSites,
            company.valuationInUsd
        )
    }

    private fun displayLaunchesData(state: LaunchesViewModel.UiState.Display) {
        // Update RecyclerView
        with(binding) {
            adapter.submitList(state.launches) { recyclerView.scrollToPosition(0) }
            swipeRefresh.isRefreshing = false
            if (state.launches.isEmpty()) {
                informationalTv.text = getString(R.string.swipe_down_to_refresh)
            } else {
                informationalTv.text = getString(R.string.blank)
            }
        }
    }

    private fun displayProgress() {
        with(binding) {
            companyDescription.text = getString(R.string.loading)
            informationalTv.text = getString(R.string.blank)
            swipeRefresh.isRefreshing = true
        }
    }

    private fun displayError(errorRes: Int) {
        with(binding) {
            companyDescription.text = getString(R.string.blank)
            informationalTv.text = getString(R.string.swipe_down_to_refresh)
            swipeRefresh.isRefreshing = false
        }
        Toast.makeText(requireContext(), getString(errorRes), Toast.LENGTH_LONG).show()
    }

}