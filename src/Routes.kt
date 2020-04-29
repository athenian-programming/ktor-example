package org.athenian

import io.ktor.application.Application
import io.ktor.application.call
import io.ktor.html.respondHtml
import io.ktor.http.ContentType
import io.ktor.http.content.resources
import io.ktor.http.content.static
import io.ktor.response.respond
import io.ktor.response.respondText
import io.ktor.routing.get
import io.ktor.routing.routing

const val greeting = "Hello world from ktor-example!"

fun Application.routes() {
  routing {
    get("/") {
      call.respondText(greeting, contentType = ContentType.Text.Plain)
    }

    get("/html") {
      call.respondHtml {
        homePage()
      }
    }

    get("/styles.css") {
      call.respondCss {
        cssContent()
      }
    }

    get("/json1") {
      call.respond(mapOf("greeting" to "Hello"))
    }

    get("/json2") {
      call.respond(mapOf("greeting" to "Hello", "name" to "Bill"))
    }

    static("/static") {
      resources("static")
    }
  }
}
