package he2b.be.mylibrary.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil3.compose.AsyncImage
import he2b.be.mylibrary.R
import he2b.be.mylibrary.ui.screens.components.ReviewSection
import he2b.be.mylibrary.ui.screens.components.StarRating
import he2b.be.mylibrary.ui.screens.components.TagPill
import he2b.be.mylibrary.ui.viewmodels.BookDetailsViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BookDetailsScreen(
    bookId: String,
    navController: NavController,
    viewModel: BookDetailsViewModel
) {
    val book by viewModel.book.collectAsState()
    val reviewText by viewModel.review.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val isEditing by viewModel.isEditing.collectAsState()

    LaunchedEffect(bookId) {
        viewModel.loadBook(bookId)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(stringResource(R.string.book_details)) },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { padding ->
        if (isLoading) {
            Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        } else {
            book?.let { b ->
                Column(
                    modifier = Modifier
                        .padding(padding)
                        .padding(16.dp)
                        .verticalScroll(rememberScrollState()),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(16.dp),
                        verticalAlignment = Alignment.Top
                    ) {
                        if (b.coverUrl != null) {
                            AsyncImage(
                                model = b.coverUrl,
                                contentDescription = null,
                                modifier = Modifier
                                    .size(120.dp, 180.dp)
                                    .clip(MaterialTheme.shapes.medium)
                                    .align(Alignment.CenterVertically),
                                contentScale = ContentScale.Fit,
                            )
                        } else {
                            AsyncImage(
                                model = R.drawable.book_stack_icon__icon_search_engine_16,
                                contentDescription = null,
                                colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.primary),
                                modifier = Modifier
                                    .size(120.dp, 180.dp)
                                    .clip(MaterialTheme.shapes.medium)
                                    .align(Alignment.CenterVertically),
                                contentScale = ContentScale.Fit,
                            )
                        }

                        Column(
                            modifier = Modifier
                                .weight(1f),
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Text(
                                text = b.title,
                                style = MaterialTheme.typography.headlineSmall,
                                maxLines = 3,
                                overflow = TextOverflow.Ellipsis
                            )
                            Text(
                                text = stringResource(
                                    R.string.authors_text,
                                    b.authors.joinToString(", ")
                                ),
                                style = MaterialTheme.typography.bodyMedium
                            )
                            b.publisher?.let {
                                Text(
                                    text = stringResource(R.string.publisher_text, it),
                                    style = MaterialTheme.typography.bodyMedium
                                )
                            }
                            b.pageCount?.takeIf { it > 0 }?.let {
                                Text(
                                    text = stringResource(R.string.page_number_text, it),
                                    style = MaterialTheme.typography.bodyMedium
                                )
                            }
                            StarRating(viewModel)
                            if (b.tags.isNotEmpty()) {
                                FlowRow(
                                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                                    modifier = Modifier.padding(top = 8.dp)
                                ) {
                                    b.tags.forEach { tag ->
                                        TagPill(
                                            text = tag.name,
                                            selected = false,
                                            onClick = {}
                                        )
                                    }
                                }
                            }
                        }
                    }

                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Text(
                            text = stringResource(R.string.description_header),
                            style = MaterialTheme.typography.titleMedium
                        )
                        Spacer(Modifier.height(16.dp))
                        Text(
                            text = if (b.description.isNullOrBlank()) {
                                stringResource(R.string.no_description)
                            } else {
                                b.description
                            },
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }

                    Spacer(Modifier.height(24.dp))

                    ReviewSection(
                        review = (book!!.review?.review ?: stringResource(R.string.no_review)),
                        reviewText = reviewText,
                        isEditing = isEditing,
                        onEditClick = { viewModel.updateEditing() },
                        onReviewChange = {review -> viewModel.updateReview(review) },
                        onSaveClick = { viewModel.saveReview() }
                    )
                }
            }
        }
    }
}
