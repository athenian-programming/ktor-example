package org.athenian

import io.ktor.application.*
import io.ktor.html.*
import io.ktor.http.*
import io.ktor.http.ContentType.Text.CSS
import io.ktor.http.content.*
import io.ktor.request.*
import io.ktor.response.*
import io.ktor.routing.get
import io.ktor.routing.post
import io.ktor.routing.routing
import kotlinx.css.*
import kotlinx.html.*
import mu.KotlinLogging

const val greeting = "Hello world from ktor-example!"
val logger = KotlinLogging.logger {}

fun Application.routes() {

  routing {

    static("/static") {
      resources("static")
    }

    get("/") {
      call.respondText(greeting, contentType = ContentType.Text.Plain)
    }

    get("/myfile") {
      call.respondHtml {
        head {
          title { +"My HTML File" }
        }
        body {
          h1 { +"Big Title" }
          p { +"Some random words" }
          img { src = "/static/ktor_logo.svg"; alt = "ktor logo"; width = "200"; height = "200" }
        }
      }
    }

    get("/html") {
      val params = call.request.queryParameters
      call.respondHtml {
        head {
          link { rel = "stylesheet"; href = "/styles.css"; type = CSS.toString() }
        }
        body {
          h1 { +"Hello ${params["first"] ?: ""} ${params["last"] ?: ""} my list is:" }

          div(classes = "mylist") {
            ul {
              (1..10).forEach { n ->
                li { +"Item $n" }
              }
            }
          }
        }
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
  }
}

suspend inline fun ApplicationCall.respondCss(builder: CSSBuilder.() -> Unit) {
  respondText(CSSBuilder().apply(builder).toString(), CSS)
}
