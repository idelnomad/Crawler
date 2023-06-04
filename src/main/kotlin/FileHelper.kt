import java.io.File
import java.net.URL
import java.nio.file.Files
import java.nio.file.Paths

class FileHelper {
    fun getLines(filepath: String): List<String> {
        if (exists(filepath)) {
            return File(filepath).readLines()
        }

        return listOf()
    }

    fun addLine(filepath: String, data: String) {
        if (!exists(filepath)) {
            File(filepath).createNewFile()
        }

        File(filepath).appendText(data+System.lineSeparator())
    }

    fun download(url: URL, filename: String) {
        url.openStream().use { Files.copy(it, Paths.get("$downloadDirectory/$filename")) }
    }

    fun exists(filepath: String): Boolean {
        return File(filepath).exists()
    }

    fun initWordList(filepath: String) {
        if (exists(filepath)) {
            var index = 1
            var newList = mutableListOf<String>()

            getLines(filepath).forEach { line ->
                val count = line.count { it == ';' }
                if (count == 0) {
                    newList.add("$index;$line")
                } else {
                    newList.add(line)
                }

                index += 1
            }

            File(filepath).writeText(newList.joinToString(System.lineSeparator()))
        } else {
            File(filepath).createNewFile()
        }
    }
}