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
import org.koin.androidx.viewmodel.ext.android.viewModel


class CompanyLaunchesFragment : Fragment() {

    private lateinit var binding: FragmentMainBinding
    private val adapter: LaunchesAdapter by inject()

    private val companyViewModel: CompanyViewModel by viewModel()
    private val launchesViewModel: LaunchesViewModel by viewModel()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentMainBinding.inflate(inflater, container, false)
        binding.recyclerView.adapter = adapter
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpObservers()
    }

    private fun setUpObservers() {
        companyViewModel.state.observe(viewLifecycleOwner) { state ->
            when (state) {
                is CompanyViewModel.UiState.Display -> displayCompanyData(state)
                is CompanyViewModel.UiState.Error -> displayError(state.errorRes)
                is CompanyViewModel.UiState.Loading -> displayProgress()
            }
        }
        launchesViewModel.state.observe(viewLifecycleOwner) { state ->
            when (state) {
                is LaunchesViewModel.UiState.Display -> displayLaunchesData(state)
                is LaunchesViewModel.UiState.Error -> displayError(state.errorRes)
                LaunchesViewModel.UiState.Loading -> displayProgress()
            }
        }
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
    }

    private fun displayProgress() {
        binding.tv.text = getString(R.string.loading)
    }

    private fun displayError(errorRes: Int) {
        binding.tv.text = getString(R.string.blank)
        Toast.makeText(requireContext(), getString(errorRes), Toast.LENGTH_SHORT).show()
    }

}