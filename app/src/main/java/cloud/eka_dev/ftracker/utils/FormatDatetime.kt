package cloud.eka_dev.ftracker.utils

import java.text.SimpleDateFormat
import java.util.Locale

// Function to format date from ISO 8601 to "Friday, 21 November 2025" format
fun formatDate(dateString: String): String {
    val inputFormat =
        SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.US)

    val outputFormat =
        SimpleDateFormat("EEEE, dd MMMM yyyy", Locale("id", "ID"))

    val date = inputFormat.parse(dateString)

    return if (date != null) {
        outputFormat.format(date)
    } else {
        ""
    }
}

fun formatDateForServer(date: String): String {
    val inputFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
    val outputFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
    val parsedDate = inputFormat.parse(date)
    return outputFormat.format(parsedDate!!)
}
