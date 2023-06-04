interface StorageInterface {
    fun next(count: Int): Map<Int, String>
    fun update(id: Int, data: VocabularyData): Boolean
}