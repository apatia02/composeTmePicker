package com.example.composetmepicker

import java.time.LocalDateTime

internal data class TimePickerItemState(
    val time: LocalDateTime,
    val isSelected: Boolean = false,
)