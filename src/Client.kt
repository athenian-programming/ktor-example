package org.athenian

import io.ktor.client.HttpClient
import io.ktor.client.engine.cio.CIO
import io.ktor.client.features.json.GsonSerializer
import io.ktor.client.features.json.JsonFeature
import io.ktor.client.features.logging.LogLevel
import io.ktor.client.features.logging.Logging
import io.ktor.client.request.get
import io.ktor.client.request.url
import io.ktor.http.ContentType.Application.Json
import io.ktor.http.contentType
import kotlinx.coroutines.runBlocking

fun main() {

  val client =
    HttpClient(CIO) {
      install(JsonFeature) {
        serializer = GsonSerializer()
      }
      install(Logging) {
        level = LogLevel.HEADERS
      }
    }


  runBlocking {

    println(
      client.get<JsonSampleClass1> {
        url("http://localhost:8080/json1")
        contentType(Json)
      }
    )

    println(
      client.get<JsonSampleClass2> {
        url("http://localhost:8080/json2")
        contentType(Json)
      }
    )
  }

}