package org.athenian

import io.ktor.http.*
import io.ktor.serialization.gson.*
import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.locations.*
import io.ktor.server.plugins.*
import io.ktor.server.request.*
import io.ktor.server.response.*
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

  install(ShutDownUrl.ApplicationCallPlugin) {
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
    exception<AuthenticationException> { call, _ ->
      call.respond(HttpStatusCode.Unauthorized)
    }
    exception<AuthorizationException> { call, _ ->
      call.respond(HttpStatusCode.Forbidden)
    }
  }
}

class AuthenticationException : RuntimeException()
class AuthorizationException : RuntimeException()
