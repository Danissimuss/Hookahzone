package Hookah.regist


import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.routing.*

fun Application.configureRegistRouting(){

    routing {
        post("/register"){
            val registControl = registControl()
            registControl.registerNewUser(call)
        }
    }
}