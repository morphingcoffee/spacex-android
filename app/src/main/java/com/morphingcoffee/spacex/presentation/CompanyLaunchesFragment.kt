package com.morphingcoffee.spacex.presentation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.morphingcoffee.spacex.R
import com.morphingcoffee.spacex.databinding.FragmentMainBinding
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
        binding.apply {
            recyclerView.adapter = adapter
            swiperefresh.setOnRefreshListener { requestDataRefresh() }
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

    private fun requestDataRefresh() {
        launchesViewModel.handleUserAction(LaunchesViewModel.UserAction.FullDataRefresh)
    }

    private fun displayCompanyData(state: CompanyViewModel.UiState.Display) {
        val company = state.company
        binding.tv.text = getString(
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
        adapter.submitList(state.launches)
        binding.swiperefresh.isRefreshing = false
        // Todo make it scroll to the top?
        //binding.recyclerView.layoutManager!!.scrollToPosition(0)
    }

    private fun displayProgress() {
        binding.tv.text = getString(R.string.loading)
        binding.swiperefresh.isRefreshing = true
    }

    private fun displayError(errorRes: Int) {
        binding.tv.text = getString(R.string.blank)
        binding.swiperefresh.isRefreshing = false
        Toast.makeText(requireContext(), getString(errorRes), Toast.LENGTH_SHORT).show()
    }

}