package dkt.dispatch.dto.api

import dkt.dispatch.dto.LoyaltyTier
import java.time.Instant
import java.util.*

data class CreateAccountApiResponse(
    val id: UUID,
    val email: String,
    val fullName: String,
    val loyaltyTier: LoyaltyTier?,
    val preferredLocation: String?,
    val note: String?,
    val createdAt: Instant,
    val updatedAt: Instant,
)
