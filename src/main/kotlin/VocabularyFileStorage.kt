import java.io.File

class VocabularyFileStorage: StorageInterface {
    override fun next(count: Int): Map<Int, String> {
        var index = 0;
        var result = mutableMapOf<Int, String>()
        file.getLines(wordsTextFile).forEach { line ->
            if (line.count { it == ';' } == 1) {
                val (l1, l2) = line.split(';')
                result.put(l1.toInt(), l2)

                if (++index > count) {
                    return result
                }
            }
        }

        return result
    }

    override fun update(id: Int, data: VocabularyData): Boolean {
        var index = 1
        var lines = mutableListOf<String>()
        var result = false

        file.getLines(wordsTextFile).forEach { line ->
            if (index == id) {
                val newLine = listOf(
                    line,
                    data.transcriptionUK,
                    data.transcriptionUS,
                    data.audioUK,
                    data.audioUS
                ).joinToString(";")

                lines.add(newLine)

                result = true
            } else {
                lines.add(line)
            }

            index += 1
        }

        File(wordsTextFile).writeText(lines.joinToString(System.lineSeparator()))

        return result
    }
}