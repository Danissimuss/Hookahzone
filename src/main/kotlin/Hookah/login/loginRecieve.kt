package Hookah.login

import kotlinx.serialization.Serializable


@Serializable
data class loginRecieve(
    val login: String,
    val password: String
)

@Serializable
data class loginResponse(
    val token: String
)
