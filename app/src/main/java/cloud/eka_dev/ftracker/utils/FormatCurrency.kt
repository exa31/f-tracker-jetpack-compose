package cloud.eka_dev.ftracker.utils


import java.text.NumberFormat
import java.util.Locale

private val rupiahFormatter = NumberFormat.getCurrencyInstance(
    Locale.forLanguageTag("id-ID")
).apply {
    maximumFractionDigits = 0 // hilangin angka dibelakang koma
}

fun formatCurrency(amount: Long): String {
    return rupiahFormatter.format(amount)
}

fun formatCurrency(amount: Int): String {
    return rupiahFormatter.format(amount.toLong())
}

fun formatToRupiah(number: String): String {
    if (number.isEmpty()) return ""
    val clean = number.replace("[^\\d]".toRegex(), "")
    if (clean.isEmpty()) return ""

    val value = clean.toLong()
    val formatter = NumberFormat.getInstance(Locale("in", "ID"))
    return "Rp" + formatter.format(value)
}
