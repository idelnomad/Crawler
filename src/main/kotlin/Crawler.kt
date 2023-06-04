import io.github.cdimascio.dotenv.dotenv

// загрузка параметров парсера
val dotenv = dotenv()

// настройки парсера
val debug = dotenv["DEBUG"].toBoolean()
val downloadDirectory: String = dotenv["DOWNLOAD_DIRECTORY"]
val wordsTextFile: String = dotenv["WORDS_TEXT_FILE"]
val nextUrl: String = dotenv["NEXT_URL"]
val updateUrl: String = dotenv["UPDATE_URL"]
val count = dotenv["COUNT"].toInt()

// работа с файлами
val file = FileHelper()

// логгер
val logger = Logger

fun main(args: Array<String>) {
    val parser = Parser()
    val storage = when (dotenv["TYPE"]) {
        "SQL" -> VocabularySqlStorage()
        "NET" -> VocabularyNetStorage()
        else -> { // по умолчанию работаем с файлами
            file.initWordList(wordsTextFile)
            VocabularyFileStorage()
        }
    }

    // первая итерация
    var words = storage.next(count)

    while (words.isNotEmpty()) {
        words.forEach { (id, word) ->
            println("$id => $word")

            val item = parser.parse(word)
            storage.update(id, item)

            if (debug) {
                logger.put("$id => $word")
                logger.put(item.toString())
            }
        }

        words = storage.next(count)
    }
}