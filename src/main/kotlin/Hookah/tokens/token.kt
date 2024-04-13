package Hookah.tokens

import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.transactions.transaction

object token: Table("token") {
    private val login = token.varchar("login", 100)
    private val id = token.varchar("id", 100)
    private val tokens = token.varchar("token", 100)


    fun insert(tokenDTO: tokenDTO) {

        transaction {
            token.insert {
                it[login] = tokenDTO.login
                it[id] = tokenDTO.id
                it[tokens] = tokenDTO.token
            }
        }

    }
}