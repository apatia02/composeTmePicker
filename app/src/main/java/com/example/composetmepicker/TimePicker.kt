package com.example.composetmepicker

import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.snapping.rememberSnapFlingBehavior
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.HorizontalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.snapshotFlow
import androidx.compose.runtime.toMutableStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.center
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch


@Composable
internal fun TimePicker(
    items: List<TimePickerItemState>,
    modifier: Modifier = Modifier,
    onTimeSelected: (TimePickerItemState) -> Unit = {},
    itemHeight: Dp = 56.dp,
) {
    val listState = rememberLazyListState()
    val coroutineScope = rememberCoroutineScope()

    val timeItems = remember(items) { items.toMutableStateList() }

    val flingBehavior = rememberSnapFlingBehavior(lazyListState = listState)

    // Сайд-эффект, реагирующий на прокрутку LazyColumn (или LazyListState).
    // Его цель — отслеживать, какой элемент оказался в центре экрана,
    // и выставлять ему флаг isSelected = true, сбрасывая у остальных.
    //
    // 1. Используется snapshotFlow для подписки на изменения индекса и смещения.
    // 2. Проверяется, входит ли центр экрана во внутренние границы элемента.
    // 3. При нахождении центрального элемента:
    //    - обновляется список timeItems, помечая нужный элемент выбранным,
    //    - вызывается onTimeSelected с выбранным временем.
    LaunchedEffect(timeItems) {
        snapshotFlow { listState.firstVisibleItemIndex to listState.firstVisibleItemScrollOffset }
            .collect {
                val viewportCenter = listState.layoutInfo.viewportSize.center.y
                val visibleItems = listState.layoutInfo.visibleItemsInfo
                var centeredItemIndex: Int? = null
                visibleItems.forEach { info ->
                    val infoOffsetWithPadding = info.offset + info.size
                    if (viewportCenter in infoOffsetWithPadding..(infoOffsetWithPadding + info.size) &&
                        info.index <= timeItems.lastIndex
                    ) {
                        centeredItemIndex = info.index
                    }
                }
                centeredItemIndex?.let { index ->
                    timeItems.forEachIndexed { i, item ->
                        timeItems[i] = item.copy(isSelected = i == index)
                    }
                    onTimeSelected(timeItems[index])
                }
            }
    }

    Box(
        modifier = modifier
            .height(168.dp)
            .background(Color.Cyan)
            .padding(horizontal = 70.dp),
        contentAlignment = Alignment.Center
    ) {
        LazyColumn(
            state = listState,
            flingBehavior = flingBehavior,
            contentPadding = PaddingValues(vertical = itemHeight),
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            itemsIndexed(timeItems) { index, timeItem ->
                TimePickerItem(
                    state = timeItem,
                    onClick = {
                        coroutineScope.launch {
                            listState.animateScrollToItem(index)
                        }
                    }
                )
            }
        }

        Column(
            verticalArrangement = Arrangement.spacedBy(itemHeight)
        ) {
            HorizontalDivider(color = Color.Black)
            HorizontalDivider(color = Color.Black)
        }
    }
}