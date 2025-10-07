package com.example.composetmepicker

import android.annotation.SuppressLint

private const val TIME_FORMAT = "%02d"
@SuppressLint("DefaultLocale")
internal fun Int.formatTime(): String = String.format(TIME_FORMAT, this)