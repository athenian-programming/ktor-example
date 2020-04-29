package org.athenian

import io.ktor.application.Application
import io.ktor.application.call
import io.ktor.application.install
import io.ktor.features.StatusPages
import io.ktor.html.respondHtml
import io.ktor.http.ContentType
import io.ktor.http.HttpStatusCode
import io.ktor.http.content.resources
import io.ktor.http.content.static
import io.ktor.locations.Location
import io.ktor.locations.get
import io.ktor.response.respond
import io.ktor.response.respondText
import io.ktor.routing.get
import io.ktor.routing.routing

fun Application.routes() {
  routing {
    get("/") {
      call.respondText("Hello world from ktor-example!", contentType = ContentType.Text.Plain)
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

    install(StatusPages) {
      exception<AuthenticationException> { cause ->
        call.respond(HttpStatusCode.Unauthorized)
      }
      exception<AuthorizationException> { cause ->
        call.respond(HttpStatusCode.Forbidden)
      }
    }

    get<MyLocation> {
      call.respondText("Location: name=${it.name}, arg1=${it.arg1}, arg2=${it.arg2}")
    }

    // Register nested routes
    get<Type.Edit> {
      call.respondText("Inside $it")
    }

    get<Type.List> {
      call.respondText("Inside $it")
    }
  }
}

@Location("/location/{name}")
class MyLocation(val name: String, val arg1: Int = 42, val arg2: String = "default")

@Location("/type/{name}")
data class Type(val name: String) {
  @Location("/edit")
  data class Edit(val type: Type)

  @Location("/list/{page}")
  data class List(val type: Type, val page: Int)
}

class AuthenticationException : RuntimeException()
class AuthorizationException : RuntimeException()
