package me.roybailey.common.test.server

import io.restassured.RestAssured.*
import io.restassured.filter.log.RequestLoggingFilter
import io.restassured.filter.log.ResponseLoggingFilter
import me.roybailey.common.testdata.UnitTestBase
import org.hamcrest.Matchers.*
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.io.FileReader

class TestApiServerTest : UnitTestBase() {


    val api = TestApiServer.createTestApiServer()


    @BeforeEach
    fun startWebApiServer() {
        val data = FileReader("$moduleTestDataFolder/sample.json").readText()
        api.start(data)
        filters(RequestLoggingFilter(), ResponseLoggingFilter())
    }


    @AfterEach
    fun stopWebApiServer() {
        api.stop()
    }


    @Test
    fun testApiServer() {

        get("${api.url}/testdata")
            .then()
            .statusCode(200)
            .body("count", equalTo(82))
    }
}
