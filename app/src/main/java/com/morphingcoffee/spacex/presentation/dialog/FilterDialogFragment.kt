package com.morphingcoffee.spacex.presentation.dialog

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo.IME_ACTION_SEARCH
import android.widget.RadioButton
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.morphingcoffee.spacex.databinding.DialogFragmentFilteringBinding
import com.morphingcoffee.spacex.domain.model.FilteringOption
import com.morphingcoffee.spacex.domain.model.LaunchStatus
import com.morphingcoffee.spacex.domain.model.SortingOption
import com.morphingcoffee.spacex.presentation.LaunchesViewModel
import org.koin.androidx.viewmodel.ext.android.sharedViewModel

class FilterDialogFragment : BottomSheetDialogFragment() {

    private lateinit var binding: DialogFragmentFilteringBinding
    private val launchesViewModel: LaunchesViewModel by sharedViewModel()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DialogFragmentFilteringBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        with(binding) {
            btnSortAscending.setOnClickListener { chooseSortingAction(SortingOption.Ascending) }
            btnSortDescending.setOnClickListener { chooseSortingAction(SortingOption.Descending) }
            btnLaunchStatusAll.setOnClickListener { chooseStatusFilteringAction(null) }
            btnLaunchStatusSuccessful.setOnClickListener {
                chooseStatusFilteringAction(FilteringOption.ByLaunchStatus(LaunchStatus.Successful))
            }
            btnLaunchStatusFailed.setOnClickListener {
                chooseStatusFilteringAction(FilteringOption.ByLaunchStatus(LaunchStatus.Failed))
            }
            yearFilterClearButton.setOnClickListener { chooseYearFilteringAction(null) }
            yearFilterEditText.setOnEditorActionListener { v, actionId, _ ->
                if (actionId == IME_ACTION_SEARCH) {
                    val year = v.text.toString().toIntOrNull()
                    chooseYearFilteringAction(year)
                    dismiss()
                    true
                } else {
                    false
                }
            }
        }
        // Setup LiveData observers. Used for updating radio buttons & Edit text fields
        launchesViewModel.sortingPreference.observe(viewLifecycleOwner) { option ->
            handleSortingChoice(option)
        }
        launchesViewModel.filterStatusPreference.observe(viewLifecycleOwner) { option ->
            handleStatusFilteringChoice(option)
        }
        launchesViewModel.filterYearPreference.observe(viewLifecycleOwner) { option ->
            handleYearFilteringChoice(option)
        }
    }

    private fun handleSortingChoice(choice: SortingOption) {
        setRadioButtonStatus(false, binding.btnSortAscendingRb, binding.btnSortDescendingRb)
        when (choice) {
            SortingOption.Ascending -> setRadioButtonStatus(true, binding.btnSortAscendingRb)
            SortingOption.Descending -> setRadioButtonStatus(true, binding.btnSortDescendingRb)
        }
    }

    private fun handleStatusFilteringChoice(
        choice: FilteringOption.ByLaunchStatus?
    ) {
        setRadioButtonStatus(
            false,
            binding.btnLaunchStatusAllRb,
            binding.btnLaunchStatusFailedRb,
            binding.btnLaunchStatusSuccessfulRb
        )
        when (choice?.status) {
            null -> {
                // Disable launch status filtering scenario
                setRadioButtonStatus(true, binding.btnLaunchStatusAllRb)
            }
            LaunchStatus.Successful -> setRadioButtonStatus(
                true,
                binding.btnLaunchStatusSuccessfulRb
            )
            LaunchStatus.Failed -> setRadioButtonStatus(true, binding.btnLaunchStatusFailedRb)
            else -> {
                // not giving the Future Launches option to the user for now
            }
        }
    }

    private fun handleYearFilteringChoice(choice: FilteringOption.ByYear?) {
        when (choice) {
            null -> {
                binding.yearFilterEditText.text.clear()
            }
            else -> {
                binding.yearFilterEditText.setText(choice.year.toString())
            }
        }
    }

    private fun chooseSortingAction(choice: SortingOption) {
        launchesViewModel.handleUserAction(
            LaunchesViewModel.UserAction.SelectSortingPreference(choice)
        )
    }

    private fun chooseStatusFilteringAction(choice: FilteringOption.ByLaunchStatus?) {
        launchesViewModel.handleUserAction(
            LaunchesViewModel.UserAction.SelectStatusFilteringPreference(choice)
        )
    }

    private fun chooseYearFilteringAction(year: Int?) {
        val choice = if (year == null) null else FilteringOption.ByYear(year)
        launchesViewModel.handleUserAction(
            LaunchesViewModel.UserAction.SelectYearFilteringPreference(choice)
        )
    }

    private fun setRadioButtonStatus(checked: Boolean, vararg views: RadioButton) {
        views.forEach { it.isChecked = checked }
    }
}