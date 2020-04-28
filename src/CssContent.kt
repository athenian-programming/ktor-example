package org.athenian

import io.ktor.application.ApplicationCall
import io.ktor.http.ContentType
import io.ktor.response.respondText
import kotlinx.css.*

fun CSSBuilder.cssContent() {
    body {
        backgroundColor = Color.red
    }

    p {
        fontSize = 2.em
    }

    rule("div.mylist") {
        color = Color.blue
    }
}

suspend inline fun ApplicationCall.respondCss(builder: CSSBuilder.() -> Unit) {
    this.respondText(CSSBuilder().apply(builder).toString(), ContentType.Text.CSS)
}
