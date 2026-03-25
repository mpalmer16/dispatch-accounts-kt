package dkt.dispatch.service

import dkt.dispatch.dto.LoyaltyTier
import dkt.dispatch.dto.service.CreateAccountServiceRequest
import dkt.dispatch.dto.service.CreateAccountServiceResponse
import dkt.dispatch.repository.AccountEntity
import dkt.dispatch.repository.DispatchAccountsRepository
import org.springframework.stereotype.Service
import java.time.Instant
import java.util.*

@Service
class DispatchAccountsService(
    private val accountsRepository: DispatchAccountsRepository,
) {

    fun createAccount(request: CreateAccountServiceRequest): CreateAccountServiceResponse? {

        val passwordHash = request.password.hashCode().toString()
        val encryptedNote = encryptNote(request.note ?: "")
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
        return CreateAccountServiceResponse(
            id = newAccount.id,
            email = newAccount.email,
            fullName = newAccount.fullName,
            loyaltyTier = newAccount.loyaltyTier,
            preferredLocation = newAccount.preferredLocation,
            note = request.note,
            createdAt = newAccount.createdAt,
            updatedAt = newAccount.updatedAt,
        )
    }

    private fun encryptNote(note: String): String {
        return note
    }

}