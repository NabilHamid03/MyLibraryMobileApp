package he2b.be.mylibrary.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Review(
    @SerialName("book_id")
    val bookId: String,
    @SerialName("user_id")
    val userId: String,
    val review: String,
    val score: Double
)
