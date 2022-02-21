package com.morphingcoffee.spacex.presentation.recyclerview

import android.content.Context
import android.view.LayoutInflater
import android.view.View
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
import com.morphingcoffee.spacex.presentation.time.ICurrentUnixTimeProvider
import java.time.LocalDateTime
import java.time.ZoneOffset
import kotlin.math.abs

class LaunchesAdapter(
    private val context: Context,
    private val imageLoader: ImageLoader,
    private val imageRequestBuilder: ImageRequest.Builder,
    private val unixTimeProvider: ICurrentUnixTimeProvider,
    asyncDifferConfig: AsyncDifferConfig<Launch>
) : ListAdapter<Launch, LaunchesAdapter.ViewHolder>(asyncDifferConfig) {

    private var onClickListener: OnLaunchSelectedListener? = null

    class ViewHolder(val binding: LaunchRowItemBinding) : RecyclerView.ViewHolder(binding.root)

    /** For delegating onclick action events **/
    fun interface OnLaunchSelectedListener {
        fun invoke(launch: Launch)
    }

    fun setOnLaunchSelectedListener(listener: OnLaunchSelectedListener?) {
        onClickListener = listener
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        val binding =
            LaunchRowItemBinding.inflate(LayoutInflater.from(viewGroup.context), viewGroup, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = getItem(position)
        val unknownFieldString = context.getString(R.string.unknown_value_field)

        val dt = if (item.launchDateTime == null) null else LocalDateTime.ofEpochSecond(
            item.launchDateTime.unixTimestamp,
            0,
            ZoneOffset.UTC
        )

        with(holder) {
            // Bind title data
            binding.title.text = item.name
            binding.dateAtTime.text =
                if (dt == null) context.getString(R.string.blank)
                else context.getString(
                    R.string.launch_field_date_template,
                    dt.year.toString(),
                    dt.monthValue.toString().padStart(2, padChar = '0'),
                    dt.dayOfMonth.toString().padStart(2, padChar = '0'),
                    dt.hour.toString().padStart(2, padChar = '0'),
                    dt.minute.toString().padStart(2, padChar = '0')
                )

            // Bind launch time data
            if (item.launchDateTime == null) {
                binding.daysSinceFromNow.visibility = View.GONE
                binding.headerDaysSinceFromNow.visibility = View.GONE
            } else {
                // positive diff means launch in the future
                val diff =
                    item.launchDateTime.unixTimestamp - (unixTimeProvider.currentTimeMillis() / 1000)
                val dayCount: Int = (diff / 86400).toInt()
                val daysPlural = context.resources.getQuantityString(R.plurals.days, abs(dayCount))

                // Heading
                val fromSinceString =
                    context.getString(if (diff > 0) R.string.launch_heading_days_from else R.string.launch_heading_days_since)
                binding.headerDaysSinceFromNow.text =
                    context.getString(R.string.launch_heading_days_template, fromSinceString)
                if (dayCount > 0) {
                    // Future launch
                    binding.daysSinceFromNow.text = context.getString(
                        R.string.launch_days_until_field_template,
                        abs(dayCount),
                        daysPlural
                    )
                } else {
                    // Past launch
                    binding.daysSinceFromNow.text = context.getString(
                        R.string.launch_days_since_field_template,
                        abs(dayCount),
                        daysPlural
                    )
                }
            }

            // Click listeners
            binding.root.setOnClickListener {
                if (itemCount > position) {
                    onClickListener?.invoke(getItem(position))
                }
            }

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