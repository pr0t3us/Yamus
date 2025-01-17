/*
 * Copyright 2017 Google Inc. All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package kg.delletenebre.yamus

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import kg.delletenebre.yamus.MediaItemData.Companion.PLAYBACK_RES_CHANGED

/**
 * [RecyclerView.Adapter] of [MediaItemData]s used by the [MediaItemFragment].
 */
class MediaItemAdapter(private val itemClickedListener: (MediaItemData) -> Unit
) : ListAdapter<MediaItemData, MediaViewHolder>(MediaItemData.diffCallback) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MediaViewHolder {
        val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.fragment_mediaitem, parent, false)
        return MediaViewHolder(view, itemClickedListener)
    }

    override fun onBindViewHolder(holder: MediaViewHolder,
                                  position: Int,
                                  payloads: MutableList<Any>) {

        val mediaItem = getItem(position)
        var fullRefresh = payloads.isEmpty()

        if (payloads.isNotEmpty()) {
            payloads.forEach { payload ->
                when (payload) {
                    PLAYBACK_RES_CHANGED -> {
                        holder.playbackState.setImageResource(mediaItem.playbackRes)
                    }
                    // If the payload wasn't understood, refresh the full item (to be safe).
                    else -> fullRefresh = true
                }
            }
        }

        // Normally we only fully refresh the list item if it's being initially bound, but
        // we might also do it if there was a payload that wasn't understood, just to ensure
        // there isn't a stale item.
        if (fullRefresh) {
            holder.item = mediaItem
            holder.titleView.text = mediaItem.title
            holder.subtitleView.text = mediaItem.subtitle
            holder.playbackState.setImageResource(mediaItem.playbackRes)

            Glide.with(holder.albumArt)
                    .load(mediaItem.albumArtUri)
                    .into(holder.albumArt)
        }
    }

    override fun onBindViewHolder(holder: MediaViewHolder, position: Int) {
        onBindViewHolder(holder, position, mutableListOf())
    }
}

class MediaViewHolder(view: View,
                      itemClickedListener: (MediaItemData) -> Unit
) : RecyclerView.ViewHolder(view) {

    val titleView: TextView by lazy { view.findViewById<TextView>(R.id.title) }
    val subtitleView: TextView by lazy { view.findViewById<TextView>(R.id.subtitle) }
    val albumArt: ImageView by lazy { view.findViewById<ImageView>(R.id.albumArt) }
    val playbackState: ImageView by lazy { view.findViewById<ImageView>(R.id.item_state) }

    var item: MediaItemData? = null

    init {
        view.setOnClickListener {
            item?.let { itemClickedListener(it) }
        }
    }
}
