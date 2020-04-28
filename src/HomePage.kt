package org.athenian

import io.ktor.http.ContentType.Text.CSS
import kotlinx.html.*

fun HTML.homePage() {
  head {
    link { rel = "stylesheet"; href = "/styles.css"; type = CSS.toString() }
  }

  body {
    h1 { +"My List" }

    div(classes = "mylist") {
      ul {
        (1..10).forEach { n ->
          li { +"Item $n" }
        }
      }
    }
  }
}