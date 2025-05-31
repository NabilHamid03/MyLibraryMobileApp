package he2b.be.mylibrary.ui.screens

import android.Manifest
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import he2b.be.mylibrary.R
import he2b.be.mylibrary.model.SortOption
import he2b.be.mylibrary.ui.screens.components.AddedBookCard
import he2b.be.mylibrary.ui.screens.components.BookCard
import he2b.be.mylibrary.ui.screens.components.LibraryToolbar
import he2b.be.mylibrary.ui.screens.components.TagsRow
import he2b.be.mylibrary.ui.viewmodels.LibraryViewModel

@Composable
fun LibraryScreen(
    viewModel: LibraryViewModel,
    navController: NavController
) {
    val isLoading by viewModel.isLoading.collectAsState()
    var isScanning by remember { mutableStateOf(false) }
    val filteredBooks by viewModel.filteredBooks.collectAsState()
    val searchQuery by viewModel.searchQuery.collectAsState()
    val tags by viewModel.tags.collectAsState()
    val selectedTag by viewModel.selectedTag.collectAsState()
    var sortMenuExpanded by remember { mutableStateOf(false) }
    val scanError by viewModel.scanError.collectAsState()
    val recentlyAddedBook by viewModel.recentlyAddedBook.collectAsState()

    //Camera Permission variables
    val context = LocalContext.current
    val snackbarHostState = remember { SnackbarHostState() }
    var shouldStartScan by remember { mutableStateOf(false) }
    var shouldShowCameraDeniedMessage by remember { mutableStateOf(false) }

    val cameraPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission(),
        onResult = { isGranted ->
            if (isGranted) {
                shouldStartScan = true
            } else {
                shouldShowCameraDeniedMessage = true
            }
        }
    )

    LaunchedEffect(shouldShowCameraDeniedMessage) {
        if (shouldShowCameraDeniedMessage) {
            snackbarHostState.showSnackbar(
                message = context.getString(R.string.camera_permission_denied)
            )
            shouldShowCameraDeniedMessage = false
        }
    }

    LaunchedEffect(Unit) {
        viewModel.loadUserBooks()
        viewModel.loadTags()
    }

    if (isScanning || shouldStartScan) {
        IsbnScannerScreen(
            onBarcodeScanned = { isbn ->
                isScanning = false
                shouldStartScan = false
                viewModel.handleScannedIsbn(isbn)
            },
            onBack = {
                isScanning = false
                shouldStartScan = false
            }
        )
        return
    }

    recentlyAddedBook?.let { book ->
        AddedBookCard(
            book = book,
            onDismiss = { viewModel.dismissRecentlyAddedBook() }
        )
    }

    if (scanError) {
        AlertDialog(
            onDismissRequest = { viewModel.dismissScanError() },
            confirmButton = {
                TextButton(onClick = { viewModel.dismissScanError() }) {
                    Text(stringResource(R.string.ok))
                }
            },
            title = { Text(stringResource(R.string.book_scan_alert_title)) },
            text = { Text(stringResource(R.string.book_scan_alert_text)) }
        )
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        LibraryToolbar(
            searchQuery = searchQuery,
            onSearch = viewModel::updateSearchQuery,
            onScanClick = { cameraPermissionLauncher.launch(Manifest.permission.CAMERA) },
            onSortClick = { sortMenuExpanded = true },
        ) {
            DropdownMenu(expanded = sortMenuExpanded, onDismissRequest = { sortMenuExpanded = false }) {
                SortOption.entries.forEach { option ->
                    DropdownMenuItem(
                        text = { Text(stringResource(option.labelResId)) },
                        onClick = {
                            viewModel.updateSortOption(option)
                            sortMenuExpanded = false
                        }
                    )
                }
            }
        }

        Spacer(Modifier.height(24.dp))
        Text(
            text = stringResource(R.string.library_screen_title),
            style = MaterialTheme.typography.titleMedium
        )
        Spacer(Modifier.height(8.dp))

        TagsRow(
            tags = tags,
            selectedTag = selectedTag,
            onTagSelected = { viewModel.selectTag(it) },
            onCreateTag = viewModel::createTag,
            onDeleteTag = viewModel::deleteTag
        )
        Spacer(Modifier.height(8.dp))

        if (isLoading) {
            Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        } else {
            LazyColumn {
                items(filteredBooks, key = { it.isbn }) { book ->
                    BookCard(
                        book = book,
                        allTags = tags,
                        onAddTag = { viewModel.addTagToBook(book.isbn, it.id) },
                        onRemoveTagFromBook = { b, tag -> viewModel.removeTagFromBook(b.isbn, tag.id) },
                        onTagSelected = viewModel::selectTag,
                        onDeleteBook = viewModel::deleteBook,
                        onClick = { navController.navigate("bookDetails/${book.id}") }
                    )
                }
            }
        }
    }

    SnackbarHost(hostState = snackbarHostState)
}