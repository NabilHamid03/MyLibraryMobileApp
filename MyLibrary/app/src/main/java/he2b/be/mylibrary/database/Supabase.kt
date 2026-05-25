package he2b.be.mylibrary.database

import he2b.be.mylibrary.BuildConfig
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.auth.Auth
import io.github.jan.supabase.auth.auth
import io.github.jan.supabase.createSupabaseClient
import io.github.jan.supabase.postgrest.Postgrest


object Supabase {
    val client: SupabaseClient = createSupabaseClient(BuildConfig.SUPABASE_URL, BuildConfig.SUPABASE_KEY) {
        install(Postgrest)
        install(Auth)
    }
    val auth = client.auth
}
