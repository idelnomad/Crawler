import java.net.URI
import java.net.http.HttpClient
import java.net.http.HttpRequest
import java.net.http.HttpResponse
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import com.fasterxml.jackson.databind.ObjectMapper

class VocabularyNetStorage: StorageInterface {
    override fun next(count: Int): Map<Int, String> {
        val client = HttpClient.newBuilder().build();
        val request = HttpRequest.newBuilder()
            .uri(URI.create("$nextUrl/$count"))
            .build();

        val response = client.send(request, HttpResponse.BodyHandlers.ofString());
        val wordData: List<WordData> = Json.decodeFromString(response.body())

        var result = mutableMapOf<Int, String>()
        wordData.forEach {
            result.put(it.id, it.en)
        }

        return result
    }

    override fun update(id: Int, data: VocabularyData): Boolean {
        val values = mapOf(
            "transcription_uk" to data.transcriptionUK,
            "transcription_us" to data.transcriptionUS,
            "audio_uk" to data.audioUK,
            "audio_us" to data.audioUS
        )

        val objectMapper = ObjectMapper()
        val requestBody: String = objectMapper.writeValueAsString(values)

        val client = HttpClient.newBuilder().build()
        val request = HttpRequest.newBuilder()
            .uri(URI.create("$updateUrl/$id"))
            .POST(HttpRequest.BodyPublishers.ofString(requestBody))
            .build()

        client.send(request, HttpResponse.BodyHandlers.ofString())

        return true
    }
}