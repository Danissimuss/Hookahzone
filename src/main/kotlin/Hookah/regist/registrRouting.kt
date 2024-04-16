package Hookah.regist


import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Application.configureRegistRouting(){

    routing {

        get ("/register"){
            call.respond("Зарегистрируйтесь прежде чем кайфануть!")
        }

        post("/register"){
            val registControl = registControl()
            registControl.registerNewUser(call)
        }
    }
}