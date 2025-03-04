package org.athenian

import io.github.oshai.kotlinlogging.KotlinLogging
import io.ktor.http.ContentType
import io.ktor.http.ContentType.Text.CSS
import io.ktor.server.application.Application
import io.ktor.server.application.ApplicationCall
import io.ktor.server.html.respondHtml
import io.ktor.server.http.content.staticResources
import io.ktor.server.request.receive
import io.ktor.server.response.respond
import io.ktor.server.response.respondText
import io.ktor.server.routing.get
import io.ktor.server.routing.post
import io.ktor.server.routing.routing
import kotlinx.css.Color
import kotlinx.css.CssBuilder
import kotlinx.css.backgroundColor
import kotlinx.css.body
import kotlinx.css.color
import kotlinx.css.em
import kotlinx.css.fontSize
import kotlinx.css.ul
import kotlinx.html.body
import kotlinx.html.div
import kotlinx.html.h1
import kotlinx.html.head
import kotlinx.html.img
import kotlinx.html.li
import kotlinx.html.link
import kotlinx.html.p
import kotlinx.html.title
import kotlinx.html.ul
import java.util.*

const val greeting = "Hello world from ktor-example!"
val logger = KotlinLogging.logger {}

fun Application.routes() {

  routing {
    staticResources("/static", "static")

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
      val postData = call.receive<JsonSampleClass>()
      logger.info { "postData = $postData" }
      call.respond(
        JsonSampleClass(
          postData.greeting.uppercase(Locale.getDefault()),
          postData.name.uppercase(Locale.getDefault())
        )
      )
    }
  }
}

suspend inline fun ApplicationCall.respondCss(builder: CssBuilder.() -> Unit) {
  respondText(CssBuilder().apply(builder).toString(), CSS)
}
