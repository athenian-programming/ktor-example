package org.athenian

import io.ktor.application.ApplicationCall
import io.ktor.http.ContentType
import io.ktor.response.respondText
import kotlinx.css.*

fun CSSBuilder.cssContent() {
    body {
        backgroundColor = Color.white
    }

    ul {
        fontSize = 1.em
    }

    rule("div.mylist") {
        color = Color.red
    }
}

suspend inline fun ApplicationCall.respondCss(builder: CSSBuilder.() -> Unit) {
    this.respondText(CSSBuilder().apply(builder).toString(), ContentType.Text.CSS)
}
