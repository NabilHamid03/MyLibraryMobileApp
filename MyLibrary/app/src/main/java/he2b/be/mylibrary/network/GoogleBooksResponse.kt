package he2b.be.mylibrary.network

data class GoogleBooksResponse(
    val items: List<GoogleBookItem>?
)

data class GoogleBookItem(
    val volumeInfo: VolumeInfo
)

data class VolumeInfo(
    val title: String,
    val authors: List<String>?,
    val description: String,
    val imageLinks: ImageLinks?,
    val publisher: String?,
    val pageCount: Int?
)

data class ImageLinks(
    val thumbnail: String?
)