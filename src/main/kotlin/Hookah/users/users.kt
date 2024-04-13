package Hookah.users

import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.transactions.transaction

object users: Table() {
    private val login = users.varchar("login", 100)
    private val password = users.varchar("password", 100)
    private val email = users.varchar("email", 100)

    fun insert(userDTO: userDTO) {

        transaction {
            users.insert {
                it[login] = userDTO.login
                it[password] = userDTO.password
                it[email] = userDTO.email
            }
        }

    }

    fun takeUser(login: String): userDTO? {
        return try {
            transaction {
                val usModel = users.select { users.login.eq(login) }.single()
                userDTO(
                    login = usModel[users.login],
                    password = usModel[password],
                    email = usModel[email]
                )
            }
        } catch (e: Exception) {
            null
        }
    }
}