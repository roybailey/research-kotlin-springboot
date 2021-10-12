package me.roybailey.common.json

import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.databind.SerializationFeature
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import me.roybailey.common.util.unixEOL
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class JacksonTest {

    data class Movie(
        var name: String,
        @JsonProperty("studio") var studioName: String,
        var rating: Float? = 1f
    )


    val mapper = jacksonObjectMapper()
        .configure(SerializationFeature.INDENT_OUTPUT, true)


    @Test
    fun whenSerializeMovie_thenSuccess() {
        val movie = Movie("Endgame", "Marvel", 9.2f)
        val serialized = mapper.writeValueAsString(movie).unixEOL()

        val json = """
      {
        "name" : "Endgame",
        "studio" : "Marvel",
        "rating" : 9.2
      }""".trimIndent().unixEOL()
        assertThat(serialized).isEqualTo(json)
    }


    @Test
    fun whenDeserializeMovie_thenSuccess() {
        val json = """{"name":"Endgame","studio":"Marvel","rating":9.2}"""
        val movie: Movie = mapper.readValue(json)

        assertThat(movie.name).isEqualTo("Endgame")
        assertThat(movie.studioName).isEqualTo("Marvel")
        assertThat(movie.rating).isEqualTo(9.2f)
    }
}
