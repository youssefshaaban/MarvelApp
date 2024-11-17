package com.example.marvalapp.ui.components

import android.util.Log
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Modifier
import com.example.domain.entity.character.Characters
import com.example.marvalapp.ui.characters.ListState
import kotlinx.collections.immutable.PersistentList
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.launch


@Composable
fun PaginatedLazyColumn(
    items: PersistentList<Characters>,  // Using PersistentList for efficient state management
    loadMoreItems: () -> Unit,  // Function to load more items
    lazyColumnListState: LazyListState,  // Track the scroll state of the LazyColumn
    buffer: Int = 2,  // Buffer to load more items when we get near the end
    listState: ListState,
    modifier: Modifier = Modifier,
    listContent: @Composable (Characters) -> Unit
) {
    val coroutineScope = rememberCoroutineScope()
    // Derived state to determine when to load more items
    val shouldLoadMore = remember {
        derivedStateOf {
            // Get the total number of items in the list
            val totalItemsCount = lazyColumnListState.layoutInfo.totalItemsCount
            // Get the index of the last visible item
            val lastVisibleItemIndex =
                lazyColumnListState.layoutInfo.visibleItemsInfo.lastOrNull()?.index ?: 0
            // Check if we have scrolled near the end of the list and more items should be loaded
            lastVisibleItemIndex >= (totalItemsCount - buffer) && listState!=ListState.PAGINATING
        }
    }
    Log.e("loadMoreItems",shouldLoadMore.value.toString())
// Launch a coroutine to load more items when shouldLoadMore becomes true
    LaunchedEffect(lazyColumnListState) {
        snapshotFlow { shouldLoadMore.value }
            .distinctUntilChanged()
            .filter { it }  // Ensure that we load more items only when needed
            .collect {
                Log.e("call", "load more")
                loadMoreItems()
            }
    }
    // LazyColumn to display the list of items
    if (listState == ListState.LOADING) {
        Loading(modifier = Modifier.fillMaxSize())
    }
    LazyColumn(
        modifier = modifier
            .fillMaxSize(),// Add padding for better visual spacing
        state = lazyColumnListState  // Pass the scroll state
    ) {
        // Render each item in the list using a unique key
        itemsIndexed(items, key = { _, item -> item.id }) { index, item ->
            listContent(item)
        }
        item(
            key = listState,
        ) {
            when (listState) {
                ListState.PAGINATING -> {
                    PaginationLoading()
                }

                ListState.PAGINATION_EXHAUST -> {
                    PaginationExhaust {
                        coroutineScope.launch {
                            lazyColumnListState.animateScrollToItem(0)
                        }
                    }
                }

                else -> {}
            }
        }

    }
}