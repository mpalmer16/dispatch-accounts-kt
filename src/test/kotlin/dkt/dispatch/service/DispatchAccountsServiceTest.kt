package dkt.dispatch.service

import dkt.dispatch.dto.LoyaltyTier
import dkt.dispatch.dto.service.CreateAccountServiceRequest
import dkt.dispatch.repository.AccountEntity
import dkt.dispatch.repository.DispatchAccountsRepository
import io.mockk.every
import io.mockk.mockk
import io.mockk.slot
import io.mockk.verify
import org.junit.jupiter.api.Disabled
import org.junit.jupiter.api.Test
import org.springframework.security.crypto.encrypt.TextEncryptor
import org.springframework.security.crypto.password.PasswordEncoder
import java.time.Instant
import java.util.UUID
import kotlin.test.assertEquals
import kotlin.test.assertNotNull

class DispatchAccountsServiceTest {

    private val accountsRepository = mockk<DispatchAccountsRepository>()
    private val passwordEncoder = mockk<PasswordEncoder>()
    private val noteEncryptor = mockk<TextEncryptor>()

    private val service = DispatchAccountsService(
        accountsRepository = accountsRepository,
        passwordEncoder = passwordEncoder,
        noteEncryptor = noteEncryptor,
    )

    @Test
    fun `createAccount hashes password encrypts note saves entity and returns decrypted note`() {
        val request = CreateAccountServiceRequest(
            email = "test-user@example.com",
            fullName = "Test User",
            password = "super-secret-password",
            loyaltyTier = LoyaltyTier.Gold,
            preferredLocation = "test-location",
            note = "Test note for encrypted storage.",
        )
        val hashedPassword = "hashed-password"
        val encryptedNote = "encrypted-note"
        val createdAt = Instant.parse("2026-03-26T16:40:00Z")
        val updatedAt = Instant.parse("2026-03-26T16:41:00Z")
        val savedEntity = slot<AccountEntity>()

        every { passwordEncoder.encode(request.password) } returns hashedPassword
        every { noteEncryptor.encrypt(request.note!!) } returns encryptedNote
        every { noteEncryptor.decrypt(encryptedNote) } returns request.note!!
        every { accountsRepository.save(capture(savedEntity)) } answers {
            val entity = savedEntity.captured
            AccountEntity(
                id = entity.id,
                email = entity.email,
                fullName = entity.fullName,
                passwordHash = entity.passwordHash,
                loyaltyTier = entity.loyaltyTier,
                preferredLocation = entity.preferredLocation,
                encryptedNote = entity.encryptedNote,
                createdAt = createdAt,
                updatedAt = updatedAt,
            )
        }

        val response = service.createAccount(request)

        assertNotNull(response)
        assertEquals(request.email, response.email)
        assertEquals(request.fullName, response.fullName)
        assertEquals(request.loyaltyTier, response.loyaltyTier)
        assertEquals(request.preferredLocation, response.preferredLocation)
        assertEquals(request.note, response.note)
        assertEquals(createdAt, response.createdAt)
        assertEquals(updatedAt, response.updatedAt)

        assertEquals(request.email, savedEntity.captured.email)
        assertEquals(request.fullName, savedEntity.captured.fullName)
        assertEquals(hashedPassword, savedEntity.captured.passwordHash)
        assertEquals(request.loyaltyTier, savedEntity.captured.loyaltyTier)
        assertEquals(request.preferredLocation, savedEntity.captured.preferredLocation)
        assertEquals(encryptedNote, savedEntity.captured.encryptedNote)

        verify(exactly = 1) { passwordEncoder.encode(request.password) }
        verify(exactly = 1) { noteEncryptor.encrypt(request.note!!) }
        verify(exactly = 1) { noteEncryptor.decrypt(encryptedNote) }
        verify(exactly = 1) { accountsRepository.save(any()) }
    }

    @Test
    @Disabled("Verify Bronze is used when loyaltyTier is null.")
    fun `createAccount defaults loyalty tier to Bronze when request tier is null`() {
    }

    @Test
    @Disabled("Verify blank notes are stored as null and not encrypted.")
    fun `createAccount stores null encryptedNote when note is blank`() {
    }

    @Test
    @Disabled("Verify null notes are returned as null and decrypt is skipped.")
    fun `createAccount returns null note when request note is null`() {
    }

    @Test
    @Disabled("Verify generated UUID is passed into the saved entity.")
    fun `createAccount generates an id before saving the entity`() {
    }

    @Test
    @Disabled("Verify repository timestamps flow through to the response.")
    fun `createAccount returns timestamps from the persisted entity`() {
    }

    @Test
    @Disabled("Verify encoder failures propagate instead of being swallowed.")
    fun `createAccount propagates password encoder exceptions`() {
    }

    @Test
    @Disabled("Verify repository failures propagate instead of being swallowed.")
    fun `createAccount propagates repository exceptions`() {
    }

    @Test
    @Disabled("Verify preferredLocation is preserved when omitted.")
    fun `createAccount allows a null preferred location`() {
    }
}
