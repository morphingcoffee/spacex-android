package com.morphingcoffee.spacex.presentation.dialog

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioButton
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.morphingcoffee.spacex.databinding.DialogFragmentFilteringBinding
import com.morphingcoffee.spacex.domain.model.LaunchStatus
import com.morphingcoffee.spacex.domain.model.SortingOption

class FilterDialogFragment : BottomSheetDialogFragment() {

    private lateinit var binding: DialogFragmentFilteringBinding

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
        binding.apply {
            btnSortAscending.setOnClickListener { handleSortingChoice(SortingOption.Ascending) }
            btnSortDescending.setOnClickListener { handleSortingChoice(SortingOption.Descending) }
            btnLaunchStatusAll.setOnClickListener { handleStatusFilteringChoice(null) }
            btnLaunchStatusSuccessful.setOnClickListener {
                handleStatusFilteringChoice(LaunchStatus.Successful)
            }
            btnLaunchStatusFailed.setOnClickListener { handleStatusFilteringChoice(LaunchStatus.Failed) }
        }
    }

    private fun handleSortingChoice(choice: SortingOption) {
        setRadioButtonStatus(false, binding.btnSortAscendingRb, binding.btnSortDescendingRb)
        when (choice) {
            SortingOption.Ascending -> setRadioButtonStatus(true, binding.btnSortAscendingRb)
            SortingOption.Descending -> setRadioButtonStatus(true, binding.btnSortDescendingRb)
        }
        // TODO update VM with sorting choice
    }

    private fun handleStatusFilteringChoice(choice: LaunchStatus?) {
        setRadioButtonStatus(
            false,
            binding.btnLaunchStatusAllRb,
            binding.btnLaunchStatusFailedRb,
            binding.btnLaunchStatusSuccessfulRb
        )
        when (choice) {
            LaunchStatus.Successful -> setRadioButtonStatus(
                true,
                binding.btnLaunchStatusSuccessfulRb
            )
            LaunchStatus.Failed -> setRadioButtonStatus(true, binding.btnLaunchStatusFailedRb)
            null -> setRadioButtonStatus(true, binding.btnLaunchStatusAllRb)
            else -> {
                // not giving the Future Launches option to the user for now
            }
        }
        // TODO update VM with filtering choice
    }

    private fun setRadioButtonStatus(checked: Boolean, vararg views: RadioButton) {
        views.forEach { it.isChecked = checked }
    }
}