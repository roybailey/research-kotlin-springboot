package me.roybailey.api.generator.configuration


class GeneratorResult(
    var name: String,
    var content: List<GeneratorFileContent> = emptyList(),
    var error: Exception? = null
) {
    fun hasContent() = content.isNotEmpty() && error == null
    fun hasError() = error != null
}


data class GeneratorFileContent(val filename:String, val content:String)
