package dkt.dispatch.dto.api

import dkt.dispatch.dto.LoyaltyTier
import java.util.*

data class GerAccountApiResponse(
    val id: UUID,
    val email: String,
    val fullName: String,
    val loyaltyTier: LoyaltyTier,
    val preferredLocation: String?,
)