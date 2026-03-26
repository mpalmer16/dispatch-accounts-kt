package dkt.dispatch.service

import dkt.dispatch.dto.LoyaltyTier
import dkt.dispatch.dto.service.CreateAccountServiceRequest
import dkt.dispatch.dto.service.CreateAccountServiceResponse
import dkt.dispatch.repository.AccountEntity
import dkt.dispatch.repository.DispatchAccountsRepository
import org.springframework.security.crypto.encrypt.TextEncryptor
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import java.time.Instant
import java.util.*

@Service
class DispatchAccountsService(
    private val accountsRepository: DispatchAccountsRepository,
    private val passwordEncoder: PasswordEncoder,
    private val noteEncryptor: TextEncryptor,
) {

    fun createAccount(request: CreateAccountServiceRequest): CreateAccountServiceResponse? {

        val passwordHash = passwordEncoder.encode(request.password)!! // should this throw an exception?
        val encryptedNote = if (request.note.isNullOrBlank()) null else noteEncryptor.encrypt(request.note)
        val loyaltyTier = request.loyaltyTier ?: LoyaltyTier.Bronze

        val newAccount = accountsRepository.save(
            AccountEntity(
                id = UUID.randomUUID(),
                email = request.email,
                fullName = request.fullName,
                passwordHash = passwordHash,
                loyaltyTier = loyaltyTier,
                preferredLocation = request.preferredLocation,
                encryptedNote = encryptedNote,
                createdAt = Instant.now(),
                updatedAt = Instant.now()
            )
        )

        val decryptedNote = if (encryptedNote.isNullOrBlank()) null else noteEncryptor.decrypt(encryptedNote)

        return CreateAccountServiceResponse(
            id = newAccount.id,
            email = newAccount.email,
            fullName = newAccount.fullName,
            loyaltyTier = newAccount.loyaltyTier,
            preferredLocation = newAccount.preferredLocation,
            note = decryptedNote,
            createdAt = newAccount.createdAt,
            updatedAt = newAccount.updatedAt,
        )
    }
}