package he2b.be.mylibrary.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Tag(
    val id: String,
    val name: String,
    @SerialName("user_id")
    val userId: String
)