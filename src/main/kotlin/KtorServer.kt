package org.athenian

import io.ktor.server.application.*
import io.ktor.server.cio.*
import io.ktor.server.engine.*

fun main(args: Array<String>) {
  val port = Integer.parseInt(System.getProperty("PORT") ?: "8080")
  embeddedServer(CIO, port = port) { module() }.start(wait = true)
}

fun Application.module(testing: Boolean = false) {
  installs()
  routes()
  locations()
}