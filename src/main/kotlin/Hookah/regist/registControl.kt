package Hookah.regist

import Hookah.HashPass.pass
import Hookah.HashPass.passDTO
import Hookah.SaltAndHash.SaltNhash
import Hookah.tokens.token
import Hookah.tokens.tokenDTO
import Hookah.users.userDTO
import Hookah.users.users
import Hookah.utils.isEmailValid

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import org.jetbrains.exposed.exceptions.ExposedSQLException
import org.jetbrains.exposed.sql.exposedLogger
import java.nio.charset.StandardCharsets
import java.util.UUID

class registControl () {

   suspend fun registerNewUser(call: ApplicationCall){

       val registrRecieve = call.receive<registrRecieve>()
       if (!registrRecieve.email.isEmailValid()){
           call.respond(HttpStatusCode.BadRequest, "Такой почты не существует")
       }


       val usDTO = users.takeUser(registrRecieve.login)

        if (usDTO != null){
            call.respond(HttpStatusCode.Conflict, "Такой пользователь уже существует")
        }else {
            val tokenz = UUID.randomUUID().toString()
            val salt = SaltNhash().getSalt()
            val onlyPass = SaltNhash().onlyHash(registrRecieve.password)
            val hashedPass = SaltNhash().hashPassword(onlyPass.toString(StandardCharsets.UTF_8),salt)

            try {

                users.insert(
                    userDTO(
                        login = registrRecieve.login,
                        password = hashedPass.toString(StandardCharsets.UTF_8),
                        email = registrRecieve.email
                    )
                )

            }catch (e:ExposedSQLException){
                call.respond(HttpStatusCode.Conflict, "Такой пользователь уже существует")
            }

            call.respond(registrResponse(token = tokenz))

            token.insert(
                tokenDTO(login = registrRecieve.login,
                    token = tokenz,
                    id = UUID.randomUUID().toString()
                )
            )

            call.respond(registrPassResponse(salt = salt.toString(StandardCharsets.UTF_8)))

            pass.insert(
                passDTO(
                    password = onlyPass,
                    salt = salt,
                    login = registrRecieve.login
                )
            )
        }

    }
}