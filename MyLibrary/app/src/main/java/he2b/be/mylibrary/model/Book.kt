package he2b.be.mylibrary.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Book(
    val id: String? = null,
    val isbn: String,
    val title: String,
    val authors: List<String>,
    val description: String?,
    @SerialName("cover_url")
    val coverUrl: String? = null,
    val publisher: String? = null,
    @SerialName("page_count")
    val pageCount: Int? = null,
    @SerialName("user_id")
    val userId: String? = null,
    val tags: List<Tag> = emptyList(),
    val review: Review? = null
)
