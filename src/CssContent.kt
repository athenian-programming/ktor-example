package org.athenian

import kotlinx.css.*

fun CSSBuilder.cssContent() {
    body {
        backgroundColor = Color.red
    }
    p {
        fontSize = 2.em
    }
    rule("p.myclass") {
        color = Color.blue
    }
}