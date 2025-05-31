package he2b.be.mylibrary.model

import androidx.annotation.StringRes
import he2b.be.mylibrary.R

enum class SortOption(@StringRes val labelResId: Int) {
    TITLE(R.string.sort_by_title),
    AUTHOR(R.string.sort_by_author),
    SCORE(R.string.sort_by_score)
}
