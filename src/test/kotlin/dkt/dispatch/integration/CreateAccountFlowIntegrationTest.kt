package dkt.dispatch.integration

import dkt.dispatch.dto.LoyaltyTier
import dkt.dispatch.repository.DispatchAccountsRepository
import org.junit.jupiter.api.Disabled
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.server.LocalServerPort
import org.springframework.http.HttpStatus
import org.springframework.test.context.DynamicPropertyRegistry
import org.springframework.test.context.DynamicPropertySource
import org.testcontainers.containers.PostgreSQLContainer
import org.testcontainers.junit.jupiter.Container
import org.testcontainers.junit.jupiter.Testcontainers
import java.net.URI
import java.net.http.HttpClient
import java.net.http.HttpRequest
import java.net.http.HttpResponse
import kotlin.test.assertEquals
import kotlin.test.assertNotEquals
import kotlin.test.assertNotNull
import kotlin.test.assertTrue

@SpringBootTest(
    webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
    properties = [
        "spring.data.redis.repositories.enabled=false",
    ]
)
@Testcontainers
class CreateAccountFlowIntegrationTest(
    @Autowired private val repository: DispatchAccountsRepository,
) {

    @LocalServerPort
    private var port: Int = 0

    @Test
    fun `create account request persists an encrypted note`() {
        val email = "flow-test-${System.currentTimeMillis()}@example.com"
        val fullName = "Test User"
        val preferredLocation = "test-location"
        val note = "Integration test note."
        val requestBody = """
            {
              "email": "$email",
              "fullName": "$fullName",
              "password": "super-secret-password",
              "loyaltyTier": "${LoyaltyTier.Gold}",
              "preferredLocation": "$preferredLocation",
              "note": "$note"
            }
        """.trimIndent()
        val response = HttpClient.newHttpClient().send(
            HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:$port/v1/accounts"))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(requestBody))
                .build(),
            HttpResponse.BodyHandlers.ofString(),
        )

        assertEquals(HttpStatus.OK.value(), response.statusCode())
        assertTrue(response.body().contains(email))

        val savedAccount = repository.findByEmail(email)

        assertNotNull(savedAccount)
        assertEquals(email, savedAccount.email)
        assertEquals(fullName, savedAccount.fullName)
        assertEquals(preferredLocation, savedAccount.preferredLocation)
        assertNotNull(savedAccount.encryptedNote)
        assertNotEquals(note, savedAccount.encryptedNote)
    }

    @Test
    @Disabled("Verify a null loyalty tier defaults to Bronze end-to-end.")
    fun `create account defaults loyalty tier when omitted`() {
    }

    @Test
    @Disabled("Verify a blank note is persisted as null end-to-end.")
    fun `create account stores null note when note is blank`() {
    }

    @Test
    @Disabled("Verify duplicate email submissions receive the expected HTTP response.")
    fun `create account rejects duplicate email addresses`() {
    }

    @Test
    @Disabled("Verify the response note is returned in decrypted form.")
    fun `create account returns the decrypted note in the API response`() {
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
