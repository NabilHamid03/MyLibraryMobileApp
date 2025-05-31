package he2b.be.mylibrary.ui.screens.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.sharp.List
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import he2b.be.mylibrary.R

@Composable
fun LibraryToolbar(
    searchQuery: String,
    onSearch: (String) -> Unit,
    onScanClick: () -> Unit,
    onSortClick: () -> Unit,
    content: @Composable () -> Unit
) {
    Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.fillMaxWidth()) {
        Button(
            onClick = { onScanClick() },
            modifier = Modifier
                .size(56.dp),
            shape = RoundedCornerShape(15.dp),
            contentPadding = PaddingValues(0.dp)
        ) {
            Icon(
                Icons.Default.AddCircle,
                contentDescription = "Scan",
                modifier = Modifier.size(32.dp)
            )
        }
        Spacer(Modifier.width(8.dp))
        OutlinedTextField(
            value = searchQuery,
            onValueChange = onSearch,
            placeholder = { Text(stringResource(R.string.library_search_field_placeholder)) },
            leadingIcon = { Icon(Icons.Default.Search, null) },
            singleLine = true,
            modifier = Modifier.weight(1f)
        )
        Spacer(Modifier.width(8.dp))
        Box {
            Button(
                onClick = { onSortClick() },
                modifier = Modifier
                    .size(56.dp),
                shape = RoundedCornerShape(15.dp),
                contentPadding = PaddingValues(0.dp)
            ) {
                Icon(
                    Icons.AutoMirrored.Sharp.List,
                    contentDescription = "Sort",
                    modifier = Modifier.size(24.dp)
                )
            }
            content()
        }
    }
}
