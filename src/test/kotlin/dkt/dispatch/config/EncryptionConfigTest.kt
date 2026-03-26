package dkt.dispatch.config

import org.junit.jupiter.api.Disabled
import org.junit.jupiter.api.Test
import org.springframework.boot.test.context.runner.ApplicationContextRunner
import java.util.function.Supplier
import kotlin.test.assertEquals
import kotlin.test.assertNotEquals
import kotlin.test.assertTrue

class EncryptionConfigTest {

    private val contextRunner = ApplicationContextRunner()
        .withBean(
            EncryptionProperties::class.java,
            Supplier {
                EncryptionProperties(
                    secret = "test-secret-value",
                    salt = "5c0744940b5c369b",
                )
            }
        )
        .withUserConfiguration(EncryptionConfig::class.java)

    @Test
    fun `noteEncryptor bean encrypts and decrypts text`() {
        contextRunner.run { context ->
            val encryptor = context.getBean("noteEncryptor", org.springframework.security.crypto.encrypt.TextEncryptor::class.java)
            val plaintext = "Test note for encryption config coverage."
            val ciphertext = encryptor.encrypt(plaintext)

            assertTrue(ciphertext.isNotBlank())
            assertNotEquals(plaintext, ciphertext)
            assertEquals(plaintext, encryptor.decrypt(ciphertext))
        }
    }

    @Test
    @Disabled("Verify the bean is created when properties are supplied through Spring property binding.")
    fun `noteEncryptor bean is available when encryption properties are bound from configuration`() {
    }

    @Test
    @Disabled("Verify context startup fails when the encryption secret is missing.")
    fun `noteEncryptor bean fails to initialize without a secret`() {
    }

    @Test
    @Disabled("Verify context startup fails when the encryption salt is missing.")
    fun `noteEncryptor bean fails to initialize without a salt`() {
    }
}
