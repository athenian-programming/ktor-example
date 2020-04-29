package org.athenian

import io.ktor.application.Application
import io.ktor.application.install
import io.ktor.features.*
import io.ktor.gson.gson
import io.ktor.locations.Locations
import io.ktor.request.path
import io.ktor.routing.routing
import io.ktor.server.cio.CIO
import io.ktor.server.engine.ShutDownUrl
import io.ktor.server.engine.embeddedServer
import org.slf4j.event.Level

object Main {
  @JvmStatic
  fun main(args: Array<String>) {
    val port = Integer.parseInt(System.getProperty("PORT") ?: "8080")
    embeddedServer(CIO, port = port) { module() }.start(wait = true)
  }
}

@kotlin.jvm.JvmOverloads
fun Application.module(testing: Boolean = false) {

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

  routing {
    routes()
  }
}

