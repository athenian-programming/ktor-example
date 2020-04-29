package org.athenian

import io.ktor.application.Application
import io.ktor.server.cio.CIO
import io.ktor.server.engine.embeddedServer

object Main {
  @JvmStatic
  fun main(args: Array<String>) {
    val port = Integer.parseInt(System.getProperty("PORT") ?: "8080")
    embeddedServer(CIO, port = port) { module() }.start(wait = true)
  }
}

@kotlin.jvm.JvmOverloads
fun Application.module(testing: Boolean = false) {
  installs()
  routes()
  locations()
}