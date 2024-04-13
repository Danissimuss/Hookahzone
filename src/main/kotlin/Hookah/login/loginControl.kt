package Hookah.login

import Hookah.HashPass.pass
import Hookah.HashPass.passDTO
import Hookah.SaltAndHash.SaltNhash
import Hookah.tokens.token
import Hookah.tokens.tokenDTO
import Hookah.users.users
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import org.jetbrains.exposed.sql.exposedLogger
import java.nio.charset.StandardCharsets
import java.security.MessageDigest
import java.util.UUID

class loginControl() {

    suspend fun performLogin(call: ApplicationCall){
        val recieve = call.receive<loginRecieve>()
        val userDTO = users.takeUser(recieve.login)
        val passContr = pass.getStoredHashedPassword(recieve.login)

        if (userDTO == null){
            call.respond(HttpStatusCode.BadRequest, "Пользователя не существует")
        }else{

            val storedSalt = pass.getStoredSalt(recieve.login)
            val hashPass = passContr?.let { SaltNhash().hashPassword(it.toString(StandardCharsets.UTF_8),storedSalt!!) }
            val tokenz = UUID.randomUUID().toString()

            if (hashPass != null && MessageDigest.isEqual(hashPass, userDTO.password.toByteArray(StandardCharsets.UTF_8))){

                token.insert(
                    tokenDTO(
                        id = UUID.randomUUID().toString(),
                        login = recieve.login,
                        token = tokenz
                    )
                )
                call.respond(loginResponse(token=tokenz))
            }else{
                call.respond(HttpStatusCode.BadRequest,"Неверный логин или пароль")
            }
        }

    }
}