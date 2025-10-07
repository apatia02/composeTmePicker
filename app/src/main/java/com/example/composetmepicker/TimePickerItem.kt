package com.example.composetmepicker

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
internal fun TimePickerItem(
    state: TimePickerItemState,
    onClick: () -> Unit = {}
) {
    val color by animateColorAsState(
        if (state.isSelected) Color.Black else Color.LightGray
    )
    Row(
        Modifier
            .fillMaxWidth()
            .clickable(enabled = !state.isSelected, onClick = onClick)
            .padding(vertical = 16.dp),
        horizontalArrangement = Arrangement.Center
    ) {
        Text(state.time.hour.formatTime(), color = color)
        Text(if (state.isSelected) ":" else " ", color = color)
        Text(state.time.minute.formatTime(), color = color)
    }
}