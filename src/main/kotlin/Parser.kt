import java.net.URL
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import org.jsoup.nodes.Element

const val site = "https://dictionary.cambridge.org"
const val page = "https://dictionary.cambridge.org/dictionary/english/"

class Parser {
    // страница
    private lateinit var document: Document
    // блок с необходимыми данными
    private lateinit var block: Element

    fun parse(word: String): VocabularyData {
        val word = normalize(word)

        // страница
        document = Jsoup.connect("${page}${word}").get()

        // блок с необходимыми данными
        val element = document.select(".pos-header.dpos-h").getOrNull(0)
        if (element != null) {
            block = element

            val title = normalize(block.select(".di-title").get(0).text())

            // искомое слово и загруженное могут не совпадать,
            // т.к. такого слова может не быть и сайт может подсунуть похожее слово
            if (word == title) {
                return VocabularyData(
                    getTranscriptionUK(),
                    getTranscriptionUS(),
                    getAudioUK(word),
                    getAudioUS(word)
                )
            }
        }

        return VocabularyData("", "", "", "")
    }

    // приведение слова к единому виду
    private fun normalize(text: String): String {
        return text.trim().lowercase()
    }

    private fun getTranscriptionUK(): String {
        var result = ""
        val element = block.select(".uk.dpron-i .pron.dpron").getOrNull(0)
        if (element != null) {
            result = element.text()
        }

        return result
    }

    private fun getTranscriptionUS(): String {
        var result = ""
        val element = block.select(".us.dpron-i .pron.dpron").getOrNull(0)
        if (element != null) {
            result = element.text()
        }

        return result
    }

    private fun getAudioUK(word: String): String {
        val element = block.select("#audio1 source").getOrNull(0)
        if (element != null) {
            val audio = "${word}_uk.mp3"
            val audioSrc = element.attr("src")
            if (!file.exists("$downloadDirectory/$audio")) {
                file.download(URL("$site/$audioSrc"), audio)
            }

            return audio
        }

        return ""
    }

    private fun getAudioUS(word: String): String {
        val element = block.select("#audio2 source").getOrNull(0)
        if (element != null) {
            val audio = "${word}_us.mp3"
            val audioSrc = element.attr("src")
            if (!file.exists("$downloadDirectory/$audio")) {
                file.download(URL("$site/$audioSrc"), audio)
            }

            return audio
        }

        return ""
    }
}