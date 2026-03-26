package dkt.dispatch.config

import org.springframework.boot.context.properties.ConfigurationProperties


@ConfigurationProperties(prefix = "app.encryption")
data class EncryptionProperties(
    val secret: String,
    val salt: String,
)