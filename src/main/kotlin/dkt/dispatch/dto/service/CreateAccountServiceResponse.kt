package dkt.dispatch.dto.service

import dkt.dispatch.dto.LoyaltyTier
import dkt.dispatch.dto.api.CreateAccountApiResponse
import java.time.Instant
import java.util.*

data class CreateAccountServiceResponse(
    val id: UUID,
    val email: String,
    val fullName: String,
    val loyaltyTier: LoyaltyTier?,
    val preferredLocation: String?,
    val note: String?,
    val createdAt: Instant,
    val updatedAt: Instant,
) {
    fun toApiResponse(): CreateAccountApiResponse = CreateAccountApiResponse(
        id,
        email,
        fullName,
        loyaltyTier,
        preferredLocation,
        note,
        createdAt,
        updatedAt
    )
}