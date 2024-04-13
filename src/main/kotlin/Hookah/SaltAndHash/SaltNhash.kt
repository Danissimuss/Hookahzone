package Hookah.SaltAndHash

import at.favre.lib.crypto.bcrypt.BCrypt
import java.nio.charset.StandardCharsets
import java.security.SecureRandom

class SaltNhash {

    fun getSalt(): ByteArray{

        val random = SecureRandom()
        val saltBytes = ByteArray(16)
        random.nextBytes(saltBytes)

        return saltBytes
    }

    fun hashPassword (password: String, salt:ByteArray):ByteArray{

        return BCrypt.withDefaults().hash(6, salt, password.toByteArray(StandardCharsets.UTF_8))

    }

    fun onlyHash (password: String):ByteArray{

        return BCrypt.withDefaults().hash(6, password.toByteArray(StandardCharsets.UTF_8))

    }
}