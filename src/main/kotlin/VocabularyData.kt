import kotlinx.serialization.Serializable

@Serializable
data class VocabularyData(
    val transcriptionUK: String,
    val transcriptionUS: String,
    val audioUK: String,
    val audioUS: String,
)