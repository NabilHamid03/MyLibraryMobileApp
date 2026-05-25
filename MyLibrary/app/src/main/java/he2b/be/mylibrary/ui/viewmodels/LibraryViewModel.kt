package he2b.be.mylibrary.ui.viewmodels

import android.graphics.BitmapFactory
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import he2b.be.mylibrary.database.BookRepository
import he2b.be.mylibrary.database.Supabase
import he2b.be.mylibrary.model.Book
import he2b.be.mylibrary.model.SortOption
import he2b.be.mylibrary.model.Tag
import he2b.be.mylibrary.network.GoogleBooksService
import io.github.jan.supabase.auth.auth
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.net.HttpURLConnection
import java.net.URL

class LibraryViewModel : ViewModel() {
    private val repository = BookRepository
    private val auth get() = Supabase.client.auth

    private val _books = MutableStateFlow<List<Book>>(emptyList())
    private val _searchQuery = MutableStateFlow("")
    val searchQuery = _searchQuery.asStateFlow()

    private val _filteredBooks = MutableStateFlow<List<Book>>(emptyList())
    val filteredBooks = _filteredBooks.asStateFlow()

    private val _tags = MutableStateFlow<List<Tag>>(emptyList())
    val tags = _tags.asStateFlow()

    private val _selectedTag = MutableStateFlow<Tag?>(null)
    val selectedTag = _selectedTag.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading = _isLoading.asStateFlow()

    private val _sortOption = MutableStateFlow(SortOption.TITLE)
    private val _scanError = MutableStateFlow(false)
    val scanError = _scanError.asStateFlow()

    private val _recentlyAddedBook = MutableStateFlow<Book?>(null)
    val recentlyAddedBook = _recentlyAddedBook.asStateFlow()


    init {
        initializeBookFiltering()
    }

    fun dismissScanError() {
        _scanError.value = false
    }

    fun dismissRecentlyAddedBook() {
        _recentlyAddedBook.value = null
    }

    fun loadTags() = viewModelScope.launch(Dispatchers.IO) {
        auth.currentUserOrNull()?.id?.let { userId ->
            _tags.value = repository.getUserTags(userId)
        }
    }

    fun selectTag(tag: Tag?) {
        _selectedTag.value = tag
    }

    fun createTag(name: String) = executeWithLoading {
        auth.currentUserOrNull()?.id?.let { userId ->
            repository.createTag(name, userId)
            loadTags()
        }
    }

    fun addTagToBook(bookId: String, tagId: String) = viewModelScope.launch(Dispatchers.IO) {
        auth.currentUserOrNull()?.id?.let { userId ->
            repository.addTagToBook(bookId, userId, tagId)
            loadUserBooks()
        }
    }

    fun deleteTag(tag: Tag) = viewModelScope.launch(Dispatchers.IO) {
        auth.currentUserOrNull()?.id?.let { userId ->
            repository.deleteTag(tag.id, userId)
            loadTags()
            loadUserBooks()
        }
    }

    fun removeTagFromBook(bookId: String, tagId: String) = viewModelScope.launch(Dispatchers.IO) {
        auth.currentUserOrNull()?.id?.let { userId ->
            repository.removeTagFromBook(bookId, userId, tagId)
            loadUserBooks()
        }
    }

    fun loadUserBooks() = executeWithLoading {
        auth.currentUserOrNull()?.id?.let { userId ->
            _books.value = repository.getUserBooks(userId)
        }
    }

    fun deleteBook(book: Book) = viewModelScope.launch(Dispatchers.IO) {
        book.id?.let { bookId ->
            repository.deleteBook(bookId)
            loadUserBooks()
        }
    }

    fun updateSortOption(option: SortOption) {
        _sortOption.value = option
    }

    fun updateSearchQuery(query: String) {
        _searchQuery.value = query
    }

    fun handleScannedIsbn(isbn: String) = executeWithLoading {
        val userId = auth.currentUserOrNull()?.id ?: return@executeWithLoading

        try {
            val book = withContext(Dispatchers.IO) {
                fetchBookDetailsFromIsbn(isbn, userId)
            }
            if (book != null) {
                repository.addScannedBook(book)
                _recentlyAddedBook.value = book
                loadUserBooks()
            } else {
                _scanError.value = true
            }
        } catch (e: Exception) {
            _scanError.value = true
        }
    }

    private suspend fun fetchBookDetailsFromIsbn(isbn: String, userId: String): Book? {
        val volumeInfo = GoogleBooksService.client
            .searchByISBN("isbn:$isbn")
            .items
            ?.firstOrNull()
            ?.volumeInfo
            ?: return null

        val coverUrl = volumeInfo.imageLinks
            ?.thumbnail
            ?.replace("http://", "https://")
            ?: fetchBookCoverUrl(isbn)

        return Book(
            id = "",
            isbn = isbn,
            title = volumeInfo.title,
            authors = volumeInfo.authors ?: listOf("Unknown"),
            description = volumeInfo.description,
            coverUrl = coverUrl,
            publisher = volumeInfo.publisher,
            pageCount = volumeInfo.pageCount,
            userId = userId
        )
    }

    private suspend fun fetchBookCoverUrl(isbn: String, size: String = "M"): String? {
        val coverUrl = "https://covers.openlibrary.org/b/isbn/$isbn-$size.jpg"
        return coverUrl.takeIf { isCoverAvailable(coverUrl) }
    }

    private suspend fun isCoverAvailable(url: String) = withContext(Dispatchers.IO) {
        try {
            (URL(url).openConnection() as? HttpURLConnection)?.run {
                doInput = true
                connect()
                inputStream.use { stream ->
                    BitmapFactory.decodeStream(stream)?.let { bitmap ->
                        bitmap.width > 10 && bitmap.height > 10
                    } ?: false
                }
            } ?: false
        } catch (_: Exception) {
            false
        }
    }

    private fun initializeBookFiltering() {
        viewModelScope.launch {
            combine(_books, _searchQuery, _selectedTag, _sortOption) { books, query, tag, sort ->
                filterAndSortBooks(books, query, tag, sort)
            }.collect { filteredBooks ->
                _filteredBooks.value = filteredBooks
            }
        }
    }

    private fun filterAndSortBooks(
        books: List<Book>,
        query: String,
        tag: Tag?,
        sort: SortOption
    ): List<Book> {
        var filteredBooks = books

        if (query.isNotBlank()) {
            filteredBooks = filteredBooks.filter { book ->
                book.title.contains(query, true) ||
                        book.authors.any { author ->
                            author.contains(query, true)
                        }
            }
        }

        tag?.let { selectedTag ->
            filteredBooks = filteredBooks.filter { book ->
                book.tags.any { bookTag ->
                    bookTag.id == selectedTag.id
                }
            }
        }

        return when (sort) {
            SortOption.TITLE -> filteredBooks.sortedBy { it.title }
            SortOption.AUTHOR -> filteredBooks.sortedBy { it.authors.firstOrNull() ?: "" }
            SortOption.SCORE -> filteredBooks.sortedByDescending { it.review?.score ?: 0.0 }
        }
    }

    private fun executeWithLoading(block: suspend () -> Unit) = viewModelScope.launch {
        _isLoading.value = true
        try {
            block()
        } finally {
            _isLoading.value = false
        }
    }
}