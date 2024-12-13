package org.athenian

import io.github.oshai.kotlinlogging.KotlinLogging
import io.ktor.client.HttpClient
import io.ktor.client.engine.cio.CIO
import io.ktor.client.plugins.HttpRequestRetry
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logging
import io.ktor.client.request.get
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.client.request.url
import io.ktor.client.statement.bodyAsText
import io.ktor.http.ContentType.Application.Json
import io.ktor.http.contentType
import io.ktor.serialization.gson.gson
import kotlinx.coroutines.runBlocking

fun main() {
  val logger = KotlinLogging.logger {}

  val client =
    HttpClient(CIO) {
      install(HttpRequestRetry) {
        retryOnServerErrors(maxRetries = 5)
        exponentialDelay()
      }

      install(ContentNegotiation) {
        gson {
          setPrettyPrinting()
          setLenient()
        }
      }

      install(Logging) {
        //level = LogLevel.ALL
        level = LogLevel.HEADERS
      }
    }

  runBlocking {
    val v1 =
      client.get {
        url("http://localhost:8080/json1")
        contentType(Json)
      }
    val v1text = v1.bodyAsText()
    logger.info { "v1 = $v1text" }

    val v2 =
      client.get {
        url("http://localhost:8080/json2")
        contentType(Json)
      }
    val v2text = v2.bodyAsText()
    logger.info { "v2 = $v2text" }

    val v3 =
      client.post {
        url("http://localhost:8080/json3")
        contentType(Json)
        setBody(JsonSampleClass("Hola", "Jill"))
      }
    val v3text = v3.bodyAsText()
    logger.info { "v3 = $v3text" }
  }

}

data class JsonSampleClass(
  val greeting: String,
  val name: String,
)
