import java.sql.Connection
import java.sql.DriverManager

class VocabularySqlStorage: StorageInterface {
    private lateinit var connection: Connection

    init {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            connection = DriverManager.getConnection("jdbc:mysql://localhost:${dotenv["DB_PORT"]}/${dotenv["DB_NAME"]}", dotenv["DB_USER"], dotenv["DB_PASSWORD"])
        } catch (e: Exception) {
            e.printStackTrace()
            println("Не удалось подключиться к БД")
        }
    }

    override fun next(count: Int): Map<Int, String> {
        val sql = "SELECT id, en FROM vocabularies WHERE loaded=0 ORDER BY id ASC LIMIT ?"
        val statement = connection.prepareStatement(sql)
        statement.setInt(1, count)

        val result = statement.executeQuery()
        var words: MutableMap<Int, String> = mutableMapOf()
        while (result.next()) {
            val id = result.getString(1).toInt()
            val word = result.getString(2).toString()

            words.put(id, word)
        }

        return words
    }

    override fun update(id: Int, data: VocabularyData): Boolean {
        val sql = "UPDATE vocabularies SET transcription_uk=?, transcription_us=?, audio_uk=?, audio_us=?, loaded=1 WHERE id=?"

        val statement = connection.prepareStatement(sql)
        statement.setString(1, data.transcriptionUK)
        statement.setString(2, data.transcriptionUS)
        statement.setString(3, data.audioUK)
        statement.setString(4, data.audioUS)
        statement.setInt(5, id)

        val result = statement.executeUpdate()
        return (result > 0)
    }
}