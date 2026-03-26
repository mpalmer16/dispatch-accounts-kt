package dkt.dispatch.repository

import dkt.dispatch.dto.LoyaltyTier
import org.junit.jupiter.api.Disabled
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.DynamicPropertyRegistry
import org.springframework.test.context.DynamicPropertySource
import org.testcontainers.containers.PostgreSQLContainer
import org.testcontainers.junit.jupiter.Container
import org.testcontainers.junit.jupiter.Testcontainers
import java.time.Instant
import java.util.UUID
import kotlin.test.assertEquals
import kotlin.test.assertNotNull

@SpringBootTest(
    properties = [
        "spring.data.redis.repositories.enabled=false",
    ]
)
@Testcontainers
class DispatchAccountsRepositoryTest(
    @Autowired private val repository: DispatchAccountsRepository,
) {

    @Test
    fun `findByEmail returns the persisted account`() {
        val createdAt = Instant.parse("2026-03-26T18:00:00Z")
        val updatedAt = Instant.parse("2026-03-26T18:01:00Z")
        val account = repository.save(
            AccountEntity(
                id = UUID.fromString("22222222-2222-2222-2222-222222222222"),
                email = "test-user@example.com",
                fullName = "Test User",
                passwordHash = "hashed-password",
                loyaltyTier = LoyaltyTier.Gold,
                preferredLocation = "test-location",
                encryptedNote = "encrypted-note",
                createdAt = createdAt,
                updatedAt = updatedAt,
            )
        )

        val found = repository.findByEmail(account.email)

        assertNotNull(found)
        assertEquals(account.id, found.id)
        assertEquals(account.email, found.email)
        assertEquals(account.fullName, found.fullName)
        assertEquals(account.passwordHash, found.passwordHash)
        assertEquals(account.loyaltyTier, found.loyaltyTier)
        assertEquals(account.preferredLocation, found.preferredLocation)
        assertEquals(account.encryptedNote, found.encryptedNote)
        assertEquals(account.createdAt, found.createdAt)
        assertEquals(account.updatedAt, found.updatedAt)
    }

    @Test
    @Disabled("Verify findByEmail returns null when the email does not exist.")
    fun `findByEmail returns null for an unknown email`() {
    }

    @Test
    @Disabled("Verify the unique email constraint rejects duplicate accounts.")
    fun `save rejects duplicate email addresses`() {
    }

    @Test
    @Disabled("Verify nullable columns can be stored as null values.")
    fun `save allows null optional columns`() {
    }

    @Test
    @Disabled("Verify loyalty tier enum values round-trip through persistence.")
    fun `save persists loyalty tier using the enum name`() {
    }

    companion object {
        @Container
        @JvmStatic
        private val postgres = PostgreSQLContainer("postgres:17-alpine")

        @JvmStatic
        @DynamicPropertySource
        fun configureProperties(registry: DynamicPropertyRegistry) {
            registry.add("spring.datasource.url", postgres::getJdbcUrl)
            registry.add("spring.datasource.username", postgres::getUsername)
            registry.add("spring.datasource.password", postgres::getPassword)
        }
    }
}
