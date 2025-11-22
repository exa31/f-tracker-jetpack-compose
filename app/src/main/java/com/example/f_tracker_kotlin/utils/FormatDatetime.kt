package com.example.f_tracker_kotlin.utils

import java.util.Locale

// Function to format date from ISO 8601 to "Friday, 21 November 2025" format
fun formatDate(dateString: String): String {
    val inputFormat =
        java.text.SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.US)

    val outputFormat =
        java.text.SimpleDateFormat("EEEE, dd MMMM yyyy", Locale("id", "ID"))

    val date = inputFormat.parse(dateString)

    return if (date != null) {
        outputFormat.format(date)
    } else {
        ""
    }
}
