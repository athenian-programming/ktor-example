package org.athenian

import io.ktor.resources.Resource
import io.ktor.server.application.Application
import io.ktor.server.resources.get
import io.ktor.server.response.respondText
import io.ktor.server.routing.routing

fun Application.locations() {
  routing {
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

@Resource("/location/{name}")
class MyLocation(val name: String, val arg1: Int = 42, val arg2: String = "default")

@Resource("/type/{name}")
data class Type(val name: String) {
  @Resource("/edit")
  data class Edit(val type: Type)

  @Resource("/list/{page}")
  data class List(val type: Type, val page: Int)
}
