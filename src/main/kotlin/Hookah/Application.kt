package Hookah

import Hookah.Https.configureHTTP
import Hookah.SslSettings.SslSettings
import Hookah.login.configureLoginRouting
import Hookah.plugins.configureRouting
import Hookah.plugins.configureSecurity
import Hookah.plugins.configureSerialization
import Hookah.plugins.configureTemplating
import Hookah.regist.configureRegistRouting
import io.ktor.client.*
import io.ktor.client.engine.apache5.*
import io.ktor.network.tls.certificates.*
import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import org.jetbrains.exposed.sql.Database
import org.slf4j.LoggerFactory
import java.io.File
import java.io.FileInputStream
import java.security.KeyStore

fun main() {

    Database.connect("jdbc:postgresql://localhost:5432/Hookah", driver = "org.postgresql.Driver",
        user = "username", password = "secret")

        val keyStoreFile = File("build/keystore.jks")
        val keyStore = if (keyStoreFile.exists()) {
        KeyStore.getInstance("JKS").apply {
            FileInputStream(keyStoreFile).use { fis ->
                load(fis, "123456".toCharArray())
            }
        }
    } else {
        buildKeyStore {
            certificate("sampleAlias") {
                password = "foobar"
                domains = listOf("127.0.0.1", "0.0.0.0", "localhost")
            }
        }.apply {
            saveToFile(keyStoreFile, "123456")
        }
    }

        val environment = applicationEngineEnvironment {
            log = LoggerFactory.getLogger("ktor.application")
            connector {
                port = 8080
            }
            sslConnector(
                keyStore = keyStore,
                keyAlias = "sampleAlias",
                keyStorePassword = { "123456".toCharArray() },
                privateKeyPassword = { "foobar".toCharArray() }) {
                host = "0.0.0.0"
                port = 8443
                keyStorePath = keyStoreFile
            }
            module(Application::module)
        }

        embeddedServer(Netty, environment).start(wait = true)
    }


fun Application.module() {
    configureHTTP()
    configureSerialization()
    configureLoginRouting()
    configureRegistRouting()

}
