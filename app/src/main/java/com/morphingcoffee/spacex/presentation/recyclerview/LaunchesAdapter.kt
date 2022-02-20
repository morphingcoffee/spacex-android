package com.morphingcoffee.spacex.presentation.recyclerview

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncDifferConfig
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import coil.ImageLoader
import coil.request.ImageRequest
import com.morphingcoffee.spacex.R
import com.morphingcoffee.spacex.databinding.LaunchRowItemBinding
import com.morphingcoffee.spacex.domain.model.Launch
import com.morphingcoffee.spacex.domain.model.LaunchStatus

class LaunchesAdapter(
    private val context: Context,
    private val imageLoader: ImageLoader,
    private val imageRequestBuilder: ImageRequest.Builder,
    asyncDifferConfig: AsyncDifferConfig<Launch>
) : ListAdapter<Launch, LaunchesAdapter.ViewHolder>(asyncDifferConfig) {

    class ViewHolder(val binding: LaunchRowItemBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        val binding =
            LaunchRowItemBinding.inflate(LayoutInflater.from(viewGroup.context), viewGroup, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = getItem(position)
        val unknownFieldString = context.getString(R.string.unknown_value_field)

        with(holder) {
            // Bind data
            binding.title.text = item.name
            binding.dateAtTime.text = item.launchDateTime ?: unknownFieldString
            binding.daysSinceFromNow.text = item.launchDateTime ?: unknownFieldString

            // Bind rocket data field
            val rocketName = item?.rocket?.name ?: unknownFieldString
            val rocketType = item?.rocket?.type ?: unknownFieldString
            val rocketDescription =
                context.getString(R.string.launch_rocket_field_template, rocketName, rocketType)
            binding.rocketNameType.text = rocketDescription

            // Bind launch status icon
            when (item.launchStatus) {
                LaunchStatus.Successful -> binding.launchStatusIcon.setBackgroundResource(R.drawable.done)
                LaunchStatus.Failed -> binding.launchStatusIcon.setBackgroundResource(R.drawable.close)
                LaunchStatus.FutureLaunch -> binding.launchStatusIcon.setBackgroundResource(R.drawable.question_mark)
            }

            // Bind patch image
            val patchImage = item.links?.patchImage
            val r = imageRequestBuilder
                .data(patchImage?.smallURL ?: patchImage?.largeURL)
                .target(binding.patchIcon)
            imageLoader.enqueue(r.build())
        }
    }
}

class LaunchesDiffUtilCallback : DiffUtil.ItemCallback<Launch>() {
    override fun areItemsTheSame(oldItem: Launch, newItem: Launch): Boolean {
        return oldItem == newItem
    }

    override fun areContentsTheSame(oldItem: Launch, newItem: Launch): Boolean {
        return oldItem.name == newItem.name && oldItem.rocket == newItem.rocket
    }
}