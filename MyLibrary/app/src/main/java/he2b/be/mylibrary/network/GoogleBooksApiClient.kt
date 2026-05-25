package he2b.be.mylibrary.network

import retrofit2.http.GET
import retrofit2.http.Query

interface GoogleBooksClient {
    @GET("volumes")
    suspend fun searchByISBN(
        @Query("q") isbn: String,
        @Query("key") apiKey: String = GoogleBooksService.API_KEY
    ): GoogleBooksResponse
}