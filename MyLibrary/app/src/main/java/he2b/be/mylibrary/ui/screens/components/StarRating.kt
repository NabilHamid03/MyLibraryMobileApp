package he2b.be.mylibrary.ui.screens.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import he2b.be.mylibrary.R
import he2b.be.mylibrary.ui.viewmodels.BookDetailsViewModel

@Composable
fun StarRating(
    viewModel: BookDetailsViewModel,
    modifier: Modifier = Modifier,
) {
    val score by viewModel.score.collectAsState()

    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically
    ) {
        for (i in 1..5) {
            val starRes = when {
                score >= i -> R.drawable.full_star
                score >= i - 0.5 -> R.drawable.half_star
                else -> R.drawable.star
            }

            Image(
                painter = painterResource(id = starRes),
                contentDescription = "Star $i",
                colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.primary),
                modifier = Modifier
                    .size(32.dp)
                    .pointerInput(Unit) {
                        detectTapGestures { offset ->
                            val tappedHalf = if (offset.x < size.width / 2) 0.5 else 1.0
                            val newRating = (i - 1) + tappedHalf
                            viewModel.updateScore(newRating)
                            viewModel.saveScore(newRating)
                        }
                    }
            )
        }
    }
}
