import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

object Logger {
    val filepath: String = dotenv["LOG_FILE"]
    val dtf = dotenv["LOG_DATE_TIME_FORMAT"]

    fun put(text: String) {
        val dt = LocalDateTime.now().format(DateTimeFormatter.ofPattern(dtf))
        file.addLine(filepath, "[$dt] $text")
    }

    fun all(): List<String> {
        return file.getLines(filepath)
    }
}