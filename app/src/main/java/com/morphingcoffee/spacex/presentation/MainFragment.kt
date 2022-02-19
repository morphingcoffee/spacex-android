package com.morphingcoffee.spacex.presentation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.morphingcoffee.spacex.R
import com.morphingcoffee.spacex.databinding.FragmentMainBinding
import org.koin.androidx.viewmodel.ext.android.viewModel

class MainFragment : Fragment() {

    private lateinit var binding: FragmentMainBinding

    private val companyInfoViewModel: CompanyInfoViewModel by viewModel()
    private val launchesViewModel: LaunchesViewModel by viewModel()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentMainBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpObservers()
    }

    private fun setUpObservers() {
        companyInfoViewModel.state.observe(viewLifecycleOwner) { state ->
            when (state) {
                is CompanyInfoViewModel.UiState.Display -> displayCompanyData(state)
                is CompanyInfoViewModel.UiState.Error -> displayError(state.errorRes)
                is CompanyInfoViewModel.UiState.Loading -> displayProgress()
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

    private fun displayCompanyData(state: CompanyInfoViewModel.UiState.Display) {
        val company = state.company
        binding.tv.text = getString(
            R.string.template_company_description,
            company.companyName,
            company.founderName,
            company.foundedYear,
            company.numOfEmployees,
            company.launchSites,
            company.valuationInUsd
        )
    }

    private fun displayLaunchesData(state: LaunchesViewModel.UiState.Display) {
        val launches = state.launches
        val launch = if (launches.isNotEmpty()) launches[0] else null
        val text: String = launch?.name ?: "No launches available"
        binding.launches.text = text
    }

    private fun displayProgress() {
        binding.tv.text = getString(R.string.loading)
        binding.launches.text = getString(R.string.loading)
    }

    private fun displayError(errorRes: Int) {
        binding.tv.text = ""
        Toast.makeText(requireContext(), getString(errorRes), Toast.LENGTH_SHORT).show()
    }

}