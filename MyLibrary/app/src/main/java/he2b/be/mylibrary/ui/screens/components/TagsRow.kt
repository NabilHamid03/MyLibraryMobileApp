package he2b.be.mylibrary.ui.screens.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import he2b.be.mylibrary.R
import he2b.be.mylibrary.model.Tag

@Composable
fun TagsRow(
    tags: List<Tag>,
    selectedTag: Tag?,
    onTagSelected: (Tag?) -> Unit,
    onCreateTag: (String) -> Unit,
    onDeleteTag: (Tag) -> Unit
) {
    var showDialog by remember { mutableStateOf(false) }
    var newTagName by remember { mutableStateOf("") }
    var tagToDelete by remember { mutableStateOf<Tag?>(null) }

    if (showDialog) {
        AlertDialog(
            onDismissRequest = { showDialog = false },
            title = { Text(stringResource(R.string.add_tag_alert_title)) },
            text = {
                OutlinedTextField(
                    value = newTagName,
                    onValueChange = { newTagName = it },
                    label = { Text(stringResource(R.string.add_tag_alert_label)) },
                    modifier = Modifier.fillMaxWidth()
                )
            },
            confirmButton = {
                Button(
                    onClick = {
                        if (newTagName.isNotBlank()) {
                            onCreateTag(newTagName)
                            showDialog = false
                            newTagName = ""
                        }
                    },
                    enabled = newTagName.isNotBlank()
                ) {
                    Text(stringResource(R.string.add))
                }
            },
            dismissButton = {
                TextButton(onClick = { showDialog = false }) {
                    Text(stringResource(R.string.cancel))
                }
            }
        )
    }

    if (tagToDelete != null) {
        AlertDialog(
            onDismissRequest = { tagToDelete = null },
            title = { Text(stringResource(R.string.delete_tag_alert_title)) },
            text = { Text(stringResource(R.string.delete_tag_alert_text, tagToDelete?.name?:"")) },
            confirmButton = {
                TextButton(onClick = {
                    tagToDelete?.let(onDeleteTag)
                    tagToDelete = null
                }) {
                    Text(stringResource(R.string.delete))
                }
            },
            dismissButton = {
                TextButton(onClick = { tagToDelete = null }) {
                    Text(stringResource(R.string.cancel))
                }
            }
        )
    }

    LazyRow(
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        contentPadding = PaddingValues(horizontal = 16.dp)
    ) {
        item {
            TagPill(
                text = stringResource(R.string.universal_tag_text),
                selected = selectedTag == null,
                onClick = { onTagSelected(null) }
            )
        }
        items(tags) { tag ->
            TagPill(
                text = tag.name,
                selected = selectedTag?.id == tag.id,
                onClick = { onTagSelected(tag) },
                onLongClick = { tagToDelete = tag }
            )
        }
        item {
            TagPill(
                text = "",
                onClick = { showDialog = true },
                icon = {
                    Icon(
                        Icons.Default.Add,
                        contentDescription = "Add a tag"
                    )
                }
            )
        }
    }
}