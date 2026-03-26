package dkt.dispatch.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.crypto.encrypt.Encryptors
import org.springframework.security.crypto.encrypt.TextEncryptor

@Configuration
class EncryptionConfig(
    private val encryptionProperties: EncryptionProperties
) {

    @Bean
    fun noteEncryptor(): TextEncryptor {
        return Encryptors.text(encryptionProperties.secret, encryptionProperties.salt)
    }
}