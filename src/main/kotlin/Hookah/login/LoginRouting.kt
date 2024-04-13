package Hookah.login

import io.ktor.server.application.*
import io.ktor.server.routing.*

fun Application.configureLoginRouting(){

    routing {
        post("/login"){
            val loginControl = loginControl()
            loginControl.performLogin(call)

        }
    }
}