package org.athenian

import io.ktor.client.*
import io.ktor.client.engine.cio.*
import io.ktor.client.plugins.*
import io.ktor.client.plugins.logging.*
import io.ktor.client.request.*
import io.ktor.http.*
import io.ktor.http.ContentType.Application.Json
import io.ktor.serialization.gson.*
import kotlinx.coroutines.runBlocking
import mu.KotlinLogging

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
    logger.info { "v1 = $v1" }

    val v2 =
      client.get {
        url("http://localhost:8080/json2")
        contentType(Json)
      }
    logger.info { "v2 = $v2" }

    val v3 =
      client.post {
        url("http://localhost:8080/json3")
        contentType(Json)
        setBody(JsonSampleClass("Hola", "Jill"))
      }
    logger.info { "v3 = $v3" }
  }

}

data class JsonSampleClass(val greeting: String, val name: String)
