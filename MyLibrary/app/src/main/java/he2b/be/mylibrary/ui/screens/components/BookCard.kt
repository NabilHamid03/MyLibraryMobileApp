package he2b.be.mylibrary.ui.screens.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import he2b.be.mylibrary.R
import he2b.be.mylibrary.model.Book
import he2b.be.mylibrary.model.Tag

@Composable
fun BookCard(
    book: Book,
    allTags: List<Tag>,
    onTagSelected: (Tag?) -> Unit,
    onAddTag: (Tag) -> Unit,
    onRemoveTagFromBook: (Book, Tag) -> Unit,
    onDeleteBook: (Book) -> Unit,
    onClick: () -> Unit,
) {
    var showAddTagDialog by remember { mutableStateOf(false) }
    var tagToRemove by remember { mutableStateOf<Tag?>(null) }
    var showDeleteBookDialog by remember { mutableStateOf(false) }

    if (tagToRemove != null) {
        AlertDialog(
            onDismissRequest = { tagToRemove = null },
            title = { Text(stringResource(R.string.remove_tag_alert_title)) },
            text = { Text(stringResource(R.string.remove_tag_alert_text, tagToRemove?.name ?: "")) },
            confirmButton = {
                TextButton(onClick = {
                    tagToRemove?.let { onRemoveTagFromBook(book, it) }
                    tagToRemove = null
                }) { Text(stringResource(R.string.remove)) }
            },
            dismissButton = {
                TextButton(onClick = { tagToRemove = null }) { Text(stringResource(R.string.cancel)) }
            }
        )
    }

    if (showDeleteBookDialog) {
        AlertDialog(
            onDismissRequest = { showDeleteBookDialog = false },
            title = { Text(stringResource(R.string.delete_book_alert_title)) },
            text = { Text(stringResource(R.string.delete_book_alert_text)) },
            confirmButton = {
                TextButton(onClick = {
                    onDeleteBook(book)
                    showDeleteBookDialog = false
                }) { Text(stringResource(R.string.delete)) }
            },
            dismissButton = {
                TextButton(onClick = { showDeleteBookDialog = false }) { Text(stringResource(R.string.cancel)) }
            }
        )
    }

    Card(
        elevation = CardDefaults.cardElevation(4.dp),
        modifier = Modifier
            .padding(8.dp)
            .fillMaxWidth()
            .pointerInput(Unit) {
                detectTapGestures(
                    onTap = { onClick() },
                    onLongPress = { showDeleteBookDialog = true }
                )
            }
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
        ) {
            if (book.coverUrl != null) {
                AsyncImage(
                    model = book.coverUrl,
                    contentDescription = null,
                    modifier = Modifier
                        .size(80.dp, 120.dp)
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
                        .size(80.dp, 120.dp)
                        .clip(MaterialTheme.shapes.medium)
                        .align(Alignment.CenterVertically),
                    contentScale = ContentScale.Fit,
                )
            }

            Spacer(modifier = Modifier.width(16.dp))

            Column(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight()
                    .align(Alignment.CenterVertically)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = book.title,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis,
                        modifier = Modifier.weight(1f)
                    )

                    book.review?.score?.let { score ->
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.padding(start = 8.dp)
                        ) {
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = "%.1f".format(score),
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold
                            )
                            Icon(
                                imageVector = Icons.Default.Star,
                                contentDescription = "Note",
                                tint = MaterialTheme.colorScheme.primary,
                                modifier = Modifier.size(16.dp)
                            )
                        }
                    }
                }

                Text(
                    text = book.authors.joinToString(),
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.padding(top = 2.dp, bottom = 8.dp)
                )

                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(4.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    items(book.tags) { tag ->
                        TagPill(
                            text = tag.name,
                            onClick = { onTagSelected(tag) },
                            onLongClick = { tagToRemove = tag }
                        )
                    }
                    item {
                        TagPill(
                            text = "",
                            icon = {
                                Icon(
                                    Icons.Default.Add, contentDescription = "Add a tag"
                                )
                            },
                            onClick = { showAddTagDialog = true }
                        )
                    }
                }
            }
        }
    }

    if (showAddTagDialog) {
        AlertDialog(
            onDismissRequest = { showAddTagDialog = false },
            title = { Text(stringResource(R.string.add_tag)) },
            text = {
                LazyColumn {
                    items(allTags) { tag ->
                        ListItem(
                            headlineContent = { Text(tag.name) },
                            modifier = Modifier.clickable {
                                onAddTag(tag)
                                showAddTagDialog = false
                            }
                        )
                    }
                }
            },
            confirmButton = {
                TextButton(onClick = { showAddTagDialog = false }) {
                    Text(stringResource(R.string.cancel))
                }
            }
        )
    }
}