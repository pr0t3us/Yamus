package kg.delletenebre.yamus.api.responses

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class WrappedPlaylist(
    @Json(name = "playlist") val playlist: Playlist = Playlist()
)