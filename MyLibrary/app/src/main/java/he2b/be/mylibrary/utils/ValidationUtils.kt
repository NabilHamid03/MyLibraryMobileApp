package he2b.be.mylibrary.utils

import android.util.Patterns

object ValidationUtils {
    private fun isEmailValid(email: String): Boolean {
        return Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }

    fun validateEmail(
        email: String,
        emptyEmailError: String,
        invalidEmailError: String
    ): String? {
        return when {
            email.isEmpty() -> emptyEmailError
            !isEmailValid(email) -> invalidEmailError
            else -> null
        }
    }

    fun validatePassword(
        password: String,
        confirmPassword: String? = null,
        emptyPasswordError: String,
        mismatchPasswordError: String
    ): String? {
        return when {
            password.isEmpty() -> emptyPasswordError
            confirmPassword != null && password != confirmPassword -> mismatchPasswordError
            else -> null
        }
    }
}
