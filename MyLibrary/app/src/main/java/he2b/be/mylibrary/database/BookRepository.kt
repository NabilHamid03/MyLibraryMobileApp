package he2b.be.mylibrary.database

import he2b.be.mylibrary.model.Book
import he2b.be.mylibrary.model.Review
import he2b.be.mylibrary.model.Tag
import io.github.jan.supabase.postgrest.postgrest
import io.github.jan.supabase.postgrest.query.Columns
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.encodeToJsonElement
import kotlinx.serialization.json.jsonObject

object BookRepository {
    private val bookTable = Supabase.client.postgrest["books"]
    private val bookTagsTable = Supabase.client.postgrest["book_tags"]
    private val tagTable = Supabase.client.postgrest["tags"]
    private val reviewTable = Supabase.client.postgrest["review"]
    private val bookDetailsView = Supabase.client.postgrest["view_book_details"]

    suspend fun addScannedBook(book: Book) {
        val json = Json.encodeToJsonElement(book).jsonObject.filterKeys { it != "id" }
        bookTable.insert(JsonObject(json))
    }

    suspend fun getUserBooks(userId: String): List<Book> {
        return bookDetailsView
            .select { filter { eq("user_id", userId) } }
            .decodeList<Book>()
    }

    suspend fun getBookById(bookId: String): Book {
        val book = bookTable.select { filter { eq("id", bookId) }; single() }.decodeAs<Book>()
        val tags = book.userId?.let { getTagsForBook(book.isbn, it) } ?: emptyList()
        val review = if (!book.id.isNullOrBlank() && !book.userId.isNullOrBlank()) getBookReview(book.id, book.userId) else null
        return book.copy(tags = tags, review = review)
    }

    suspend fun deleteBook(bookId: String) {
        bookTable.delete { filter { eq("id", bookId) } }
    }

    suspend fun getUserTags(userId: String): List<Tag> =
        tagTable.select { filter { eq("user_id", userId) } }.decodeList()

    suspend fun addTagToBook(bookId: String, userId: String, tagId: String) {
        bookTagsTable.insert(mapOf("isbn" to bookId, "user_id" to userId, "tag_id" to tagId))
    }

    suspend fun createTag(name: String, userId: String) {
        tagTable.insert(mapOf("name" to name, "user_id" to userId))
    }

    private suspend fun getTagsForBook(isbn: String, userId: String): List<Tag> {
        val bookTags = bookTagsTable.select { filter { eq("isbn", isbn); eq("user_id", userId) } }.decodeList<Map<String, String>>()
        val tagIds = bookTags.mapNotNull { it["tag_id"] }
        return if (tagIds.isNotEmpty()) tagTable.select { filter { isIn("id", tagIds) } }.decodeList() else emptyList()
    }

    suspend fun deleteTag(tagId: String, userId: String) {
        tagTable.delete { filter { eq("id", tagId); eq("user_id", userId) } }
    }

    suspend fun removeTagFromBook(isbn: String, userId: String, tagId: String) {
        bookTagsTable.delete { filter { eq("isbn", isbn); eq("user_id", userId); eq("tag_id", tagId) } }
    }

    private suspend fun getBookReview(bookId: String, userId: String): Review? =
        reviewTable.select(columns = Columns.list("book_id, user_id, review, score")) { filter { eq("book_id", bookId); eq("user_id", userId) } }
            .decodeList<Review>().firstOrNull()

    suspend fun upsertBookScore(bookId: String, userId: String, score: Double) {
        val current = getBookReview(bookId, userId)
        val data = Review(bookId, userId, current?.review ?: "", score)
        reviewTable.upsert(data) { onConflict = "book_id,user_id" }
    }

    suspend fun upsertBookReviewText(bookId: String, userId: String, reviewText: String) {
        val current = getBookReview(bookId, userId)
        val data = Review(bookId, userId, reviewText, current?.score ?: 0.0)
        reviewTable.upsert(data) { onConflict = "book_id,user_id" }
    }
}
