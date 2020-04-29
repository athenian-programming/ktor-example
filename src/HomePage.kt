package org.athenian

import io.ktor.http.ContentType.Text.CSS
import io.ktor.http.Parameters
import kotlinx.html.*

fun HTML.homePage(params: Parameters) {

  head {
    link { rel = "stylesheet"; href = "/styles.css"; type = CSS.toString() }
  }

  body {
    h1 { +"Hello ${params["first"]} ${params["last"]} my list is:" }

    div(classes = "mylist") {
      ul {
        (1..10).forEach { n ->
          li { +"Item $n" }
        }
      }
    }
  }
}