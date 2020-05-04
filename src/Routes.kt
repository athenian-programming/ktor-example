package org.athenian

import io.ktor.application.Application
import io.ktor.application.ApplicationCall
import io.ktor.application.call
import io.ktor.html.respondHtml
import io.ktor.http.ContentType
import io.ktor.http.content.resources
import io.ktor.http.content.static
import io.ktor.request.receive
import io.ktor.response.respond
import io.ktor.response.respondText
import io.ktor.routing.get
import io.ktor.routing.post
import io.ktor.routing.routing
import kotlinx.css.*
import mu.KotlinLogging

const val greeting = "Hello world from ktor-example!"
val logger = KotlinLogging.logger {}

fun Application.routes() {

  routing {
    get("/") {
      call.respondText(greeting, contentType = ContentType.Text.Plain)
    }

    get("/html") {
      val params = call.request.queryParameters
      call.respondHtml {
        homePage(params)
      }
    }

    get("/styles.css") {
      call.respondCss {
        body {
          backgroundColor = Color.blue
        }

        ul {
          fontSize = 3.em
        }

        rule("div.mylist") {
          color = Color.green
        }
      }
    }

    get("/json1") {
      call.respond(mapOf("greeting" to "Hello"))
    }

    get("/json2") {
      call.respond(mapOf("greeting" to "Hello", "name" to "Bill"))
    }

    post("/json3") {
      val postData = call.receive<JsonSampleClass2>()
      logger.info { "postData = $postData" }
      call.respond(JsonSampleClass2(postData.greeting.toUpperCase(), postData.name.toUpperCase()))
    }

    static("/static") {
      resources("static")
    }
  }
}

suspend inline fun ApplicationCall.respondCss(builder: CSSBuilder.() -> Unit) {
  respondText(CSSBuilder().apply(builder).toString(), ContentType.Text.CSS)
}
