package Hookah.HashPass

import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.transactions.transaction

object pass: Table ("openpass") {
        private val password = pass.binary("hashedPass")
        private val salt = pass.binary("salt")
        private val login = pass.varchar("login", 200)


        fun insert(passDTO: passDTO) {

            transaction {
                pass.insert {
                    it[password] = passDTO.password
                    it[salt] = passDTO.salt
                    it[login] = passDTO.login
                }
            }

        }

    fun getStoredHashedPassword(login: String): ByteArray? {
        return try {
            transaction {
                pass.select { pass.login.eq(login) }.singleOrNull()?.get(password)
            }
        } catch (e: Exception) {
            null
        }
    }

    fun getStoredSalt(login: String): ByteArray? {
        return try {
            transaction {
                pass.select { pass.login.eq(login) }.singleOrNull()?.get(salt)
            }
        } catch (e: Exception) {
            null
        }
    }

}