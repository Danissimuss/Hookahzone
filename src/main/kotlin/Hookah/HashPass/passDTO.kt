package Hookah.HashPass

import kotlinx.serialization.Serializable

@Serializable
data class passDTO(
    val password: ByteArray,
    val salt: ByteArray,
    val login: String
)
