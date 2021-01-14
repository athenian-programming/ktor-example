package org.athenian

import io.ktor.client.*
import io.ktor.client.engine.cio.*
import io.ktor.client.features.json.*
import io.ktor.client.features.logging.*
import io.ktor.client.request.*
import io.ktor.http.*
import io.ktor.http.ContentType.Application.Json
import kotlinx.coroutines.runBlocking
import mu.KotlinLogging

fun main() {

  val logger = KotlinLogging.logger {}

  val client =
    HttpClient(CIO) {
      install(JsonFeature) {
        serializer = GsonSerializer()
      }
      install(Logging) {
        level = LogLevel.ALL
        //level = LogLevel.HEADERS
      }
    }

  runBlocking {

    val v1 =
      client.get<JsonSampleClass1> {
        url("http://localhost:8080/json1")
        contentType(Json)
      }
    logger.info { "v1 = $v1" }

    val v2 =
      client.get<JsonSampleClass2> {
        url("http://localhost:8080/json2")
        contentType(Json)
      }
    logger.info { "v2 = $v2" }

    val v3 =
      client.post<JsonSampleClass2> {
        url("http://localhost:8080/json3")
        contentType(Json)
        body = JsonSampleClass2("Hola", "Jill")
      }
    logger.info { "v3 = $v3" }
  }

}

data class JsonSampleClass1(val greeting: String)
data class JsonSampleClass2(val greeting: String, val name: String)
