package he2b.be.mylibrary.network

import com.squareup.moshi.Moshi
import he2b.be.mylibrary.BuildConfig
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

object GoogleBooksService {
    private const val BASE_URL = "https://www.googleapis.com/books/v1/"
    const val API_KEY = BuildConfig.GOOGLE_KEY


    val client: GoogleBooksClient

    init {
        val moshi = Moshi.Builder()
            .build()

        val jsonConverter = MoshiConverterFactory.create(moshi)

        val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(jsonConverter)
            .build()

        client = retrofit.create(GoogleBooksClient::class.java)
    }
}