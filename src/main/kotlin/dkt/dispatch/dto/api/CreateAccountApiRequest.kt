package dkt.dispatch.dto.api

import dkt.dispatch.dto.LoyaltyTier
import dkt.dispatch.dto.service.CreateAccountServiceRequest

data class CreateAccountApiRequest(
    val email: String,
    val fullName: String,
    val password: String,
    val loyaltyTier: LoyaltyTier?,
    val preferredLocation: String?,
    val note: String?,
) {
    fun toServiceRequest(): CreateAccountServiceRequest = CreateAccountServiceRequest(
        email,
        fullName,
        password,
        loyaltyTier,
        preferredLocation,
        note
    )
}


