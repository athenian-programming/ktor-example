package org.athenian

import io.ktor.application.*
import io.ktor.features.*
import io.ktor.gson.*
import io.ktor.http.*
import io.ktor.locations.*
import io.ktor.request.*
import io.ktor.response.*
import io.ktor.server.engine.*
import org.slf4j.event.Level

fun Application.installs() {
  install(Compression) {
    gzip {
      priority = 1.0
    }
    deflate {
      priority = 10.0
      minimumSize(1024) // condition
    }
  }

  install(CallLogging) {
    level = Level.INFO
    filter { call -> call.request.path().startsWith("/") }
  }

  install(ShutDownUrl.ApplicationCallFeature) {
    shutDownUrl = "/ktor/application/shutdown"
    exitCodeSupplier = { 0 } // ApplicationCall.() -> Int
  }

  install(ContentNegotiation) {
    gson {
      setPrettyPrinting()
      setLenient()
    }
  }

  install(Locations) {
  }

  install(StatusPages) {
    exception<AuthenticationException> { cause ->
      call.respond(HttpStatusCode.Unauthorized)
    }
    exception<AuthorizationException> { cause ->
      call.respond(HttpStatusCode.Forbidden)
    }
  }
}

class AuthenticationException : RuntimeException()
class AuthorizationException : RuntimeException()
