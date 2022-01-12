package com.libertosforever.note.utils

import java.text.SimpleDateFormat
import java.util.*

object TimeManager {

    fun getCurrentTime(): String {
        val formatter = SimpleDateFormat("hh:mm - yyyy/MM/dd", Locale.getDefault())
        return formatter.format(Calendar.getInstance().time)
    }
}