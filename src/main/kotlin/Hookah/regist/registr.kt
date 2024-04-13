package Hookah.regist

import kotlinx.serialization.Serializable


@Serializable
data class registrRecieve(
    val login: String,
    val password: String,
    val email: String
)

@Serializable
data class registrResponse(
    val token: String
)

@Serializable
data class registrPassResponse(
    val salt: String
)