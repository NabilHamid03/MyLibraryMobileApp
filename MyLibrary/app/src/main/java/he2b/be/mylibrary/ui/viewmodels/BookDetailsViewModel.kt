package he2b.be.mylibrary.ui.viewmodels

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import he2b.be.mylibrary.database.BookRepository
import he2b.be.mylibrary.model.Book
import he2b.be.mylibrary.model.Review
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class BookDetailsViewModel : ViewModel() {

    private val _book = MutableStateFlow<Book?>(null)
    val book: StateFlow<Book?> = _book

    private val _review = MutableStateFlow("")
    val review: StateFlow<String> = _review

    private val _score = MutableStateFlow(0.0)
    val score: StateFlow<Double> = _score

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _isEditing = MutableStateFlow(false)
    val isEditing: StateFlow<Boolean> = _isEditing

    fun loadBook(bookId: String) {
        _isLoading.value = true

        viewModelScope.launch(Dispatchers.IO) {
            try {
                val book = BookRepository.getBookById(bookId)
                withContext(Dispatchers.Main) {
                    _book.value = book
                    _score.value = book.review?.score ?: 0.0
                    _review.value = book.review?.review ?: ""
                    _isLoading.value = false
                }
            } catch (e: Exception) {
                Log.e("BookViewModel", "Error loading book", e)
                withContext(Dispatchers.Main) {
                    _isLoading.value = false
                }
            }
        }
    }


    fun updateEditing() {
        _isEditing.value = !isEditing.value
    }

    fun updateReview(newReview: String) {
        _review.value = newReview
    }

    fun updateScore(newRating: Double) {
        _score.value = newRating
    }

    fun saveScore(newScore: Double) {
        _isLoading.value = true
        val currentBook = _book.value
        val userId = currentBook?.userId
        val bookId = currentBook?.id

        Log.d("ReviewDebug", "Updating score for bookId=$bookId, userId=$userId")

        if (bookId == null || userId == null) {
            Log.e("ReviewDebug", "bookId or userId is null!")
            _isLoading.value = false
            return
        }

        viewModelScope.launch(Dispatchers.IO) {
            try {
                BookRepository.upsertBookScore(bookId, userId, newScore)
            } catch (e: Exception) {
                Log.e("ReviewDebug", "Error while updating score", e)
            } finally {
                withContext(Dispatchers.Main) {
                    val currBook = _book.value
                    val currentReview = currBook?.review

                    if (currBook != null && currentReview != null) {
                        val updatedReview = currentReview.copy(score = newScore)
                        _book.value = currBook.copy(review = updatedReview)
                    }

                    _isLoading.value = false
                }
            }
        }
    }

    fun saveReview() {
        _isLoading.value = true
        val currentBook = _book.value
        val userId = currentBook?.userId
        val bookId = currentBook?.id

        Log.d("ReviewDebug", "Updating review for bookId=$bookId, userId=$userId")

        if (bookId == null || userId == null) {
            Log.e("ReviewDebug", "bookId or userId is null!")
            _isLoading.value = false
            return
        }

        viewModelScope.launch(Dispatchers.IO) {
            try {
                BookRepository.upsertBookReviewText(bookId, userId, _review.value)
            } catch (e: Exception) {
                Log.e("ReviewDebug", "Error while updating review", e)
            } finally {
                withContext(Dispatchers.Main) {
                    val currBook = _book.value
                    val currentReview = currBook?.review

                    val newReviewObj = if (currentReview != null) {
                        currentReview.copy(review = _review.value)
                    } else {
                        Review(
                            review = _review.value,
                            bookId = _book.value?.id?:"",
                            userId = _book.value?.userId?:"",
                            score = 0.0,
                        )
                    }
                    if (currBook != null) {
                        _book.value = currBook.copy(review = newReviewObj)
                    }
                    _isEditing.value = false
                    _isLoading.value = false
                }
            }
        }
    }
}
