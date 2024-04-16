package Hookah.login

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Application.configureLoginRouting(){

    routing {

        get ("/login"){
            call.respond("Введите имя пользователя и пароль!")
        }

        post ("/login") {
            val loginControl = loginControl()
            loginControl.performLogin(call)

        }
    }
}