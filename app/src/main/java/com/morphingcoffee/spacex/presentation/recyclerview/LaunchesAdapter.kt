package com.morphingcoffee.spacex.presentation.recyclerview

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.AsyncDifferConfig
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import coil.ImageLoader
import coil.request.ImageRequest
import com.morphingcoffee.spacex.R
import com.morphingcoffee.spacex.domain.model.Launch
import com.morphingcoffee.spacex.domain.model.LaunchStatus

class LaunchesAdapter(
    private val context: Context,
    private val imageLoader: ImageLoader,
    private val imageRequestBuilder: ImageRequest.Builder,
    asyncDifferConfig: AsyncDifferConfig<Launch>
) : ListAdapter<Launch, LaunchesAdapter.ViewHolder>(asyncDifferConfig) {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val title: TextView = view.findViewById(R.id.title)
        val dateAtTime: TextView = view.findViewById(R.id.dateAtTime)
        val daysFromNow: TextView = view.findViewById(R.id.daysSinceFromNow)
        val rocketNameType: TextView = view.findViewById(R.id.rocketNameType)
        val icon: ImageView = view.findViewById(R.id.patchIcon)
        val statusIcon: ImageView = view.findViewById(R.id.launchStatusIcon)
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.launch_row_item, viewGroup, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = getItem(position)
        val unknownFieldString = context.getString(R.string.unknown_value_field)

        // Bind data
        holder.title.text = item.name
        holder.dateAtTime.text = item.launchDateTime ?: unknownFieldString
        holder.daysFromNow.text = item.launchDateTime ?: unknownFieldString

        // Bind rocket data field
        val rocketName = item?.rocket?.name ?: unknownFieldString
        val rocketType = item?.rocket?.type ?: unknownFieldString
        val rocketDescription =
            context.getString(R.string.launch_rocket_field_template, rocketName, rocketType)
        holder.rocketNameType.text = rocketDescription

        // Bind launch status icon
        when (item.launchStatus) {
            LaunchStatus.Successful -> holder.statusIcon.setBackgroundResource(R.drawable.done)
            LaunchStatus.Failed -> holder.statusIcon.setBackgroundResource(R.drawable.close)
            LaunchStatus.FutureLaunch -> holder.statusIcon.setBackgroundResource(R.drawable.question_mark)
        }

        // Bind patch image
        val patchImage = item.links?.patchImage
        val r = imageRequestBuilder
            .data(patchImage?.smallURL ?: patchImage?.largeURL)
            .target(holder.icon)
        imageLoader.enqueue(r.build())
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