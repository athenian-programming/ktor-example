package org.athenian

import com.google.gson.Strictness
import io.ktor.http.HttpStatusCode
import io.ktor.serialization.gson.gson
import io.ktor.server.application.Application
import io.ktor.server.application.install
import io.ktor.server.engine.ShutDownUrl
import io.ktor.server.plugins.calllogging.CallLogging
import io.ktor.server.plugins.compression.Compression
import io.ktor.server.plugins.compression.deflate
import io.ktor.server.plugins.compression.gzip
import io.ktor.server.plugins.compression.minimumSize
import io.ktor.server.plugins.contentnegotiation.ContentNegotiation
import io.ktor.server.plugins.statuspages.StatusPages
import io.ktor.server.request.path
import io.ktor.server.resources.Resources
import io.ktor.server.response.respond
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
      setStrictness(Strictness.LENIENT)
    }
  }

  install(Resources) {
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
