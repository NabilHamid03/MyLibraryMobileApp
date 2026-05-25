package he2b.be.mylibrary.database

import he2b.be.mylibrary.BuildConfig
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.auth.Auth
import io.github.jan.supabase.auth.auth
import io.github.jan.supabase.createSupabaseClient
import io.github.jan.supabase.postgrest.Postgrest


object Supabase {
    private const val URL = "https://vjfssputplzkngryzwrh.supabase.co"
    private const val KEY = BuildConfig.SUPABASE_KEY

    val client: SupabaseClient = createSupabaseClient(URL, KEY) {
        install(Postgrest)
        install(Auth)
    }
    val auth = client.auth
}
