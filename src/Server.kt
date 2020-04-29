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
    //EngineMain.main(args)
    val port = Integer.parseInt(System.getProperty("PORT") ?: "8080")
    embeddedServer(CIO, port = port) { module() }.start(wait = true)
  }
}

@Suppress("unused") // Referenced in application.conf
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
    // The URL that will be intercepted (you can also use the application.conf's ktor.deployment.shutdown.url key)
    shutDownUrl = "/ktor/application/shutdown"
    // A function that will be executed to get the exit code of the process
    exitCodeSupplier = { 0 } // ApplicationCall.() -> Int
  }

  install(ContentNegotiation) {
    gson {}
  }

  install(Locations) {
  }

  routing {
    routes()
  }
}

class AuthenticationException : RuntimeException()
class AuthorizationException : RuntimeException()

data class JsonSampleClass1(val greeting: String)
data class JsonSampleClass2(val greeting: String, val name: String)

