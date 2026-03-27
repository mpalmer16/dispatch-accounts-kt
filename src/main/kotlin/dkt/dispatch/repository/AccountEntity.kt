package dkt.dispatch.repository

import dkt.dispatch.dto.LoyaltyTier
import dkt.dispatch.dto.api.GerAccountApiResponse
import jakarta.persistence.*
import java.time.Instant
import java.util.*

@Entity
@Table(name = "accounts")
class AccountEntity(
    @Id
    val id: UUID,

    @Column(nullable = false, unique = true)
    val email: String,

    @Column(name = "full_name", nullable = false)
    val fullName: String,

    @Column(name = "password_hash", nullable = false)
    val passwordHash: String,

    @Enumerated(EnumType.STRING)
    @Column(name = "loyalty_tier", nullable = false)
    val loyaltyTier: LoyaltyTier,

    @Column(name = "preferred_location")
    val preferredLocation: String?,

    @Column(name = "encrypted_note")
    val encryptedNote: String?,

    @Column(name = "created_at", nullable = false)
    val createdAt: Instant,

    @Column(name = "updated_at", nullable = false)
    val updatedAt: Instant,
)

fun AccountEntity.toAccountResponse(): GerAccountApiResponse {
    return GerAccountApiResponse(
        id = id,
        email = email,
        fullName = fullName,
        loyaltyTier = loyaltyTier,
        preferredLocation = preferredLocation
    )
}