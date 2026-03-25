package dkt.dispatch.dto.service

import dkt.dispatch.dto.LoyaltyTier


data class CreateAccountServiceRequest(
    val email: String,
    val fullName: String,
    val password: String,
    val loyaltyTier: LoyaltyTier?,
    val preferredLocation: String?,
    val note: String?,
)