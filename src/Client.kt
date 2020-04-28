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

val client = HttpClient(CIO) {
    install(JsonFeature) {
        serializer = GsonSerializer()
    }
    install(Logging) {
        level = LogLevel.HEADERS
    }
}

fun main() {
    runBlocking {
        println(
            client.get<JsonSampleClass1> {
                url("http://localhost:8080/json/gson1")
                contentType(Json)
            }
        )

        println(
            client.get<JsonSampleClass2> {
                url("http://localhost:8080/json/gson2")
                contentType(Json)
            }
        )


        /*
        val message2 = client.post<JsonSampleClass> {
            url("http://localhost:8080/path/to/endpoint")
            contentType(Json)
            body = JsonSampleClass(hello = "world")
        }
         */
    }

}