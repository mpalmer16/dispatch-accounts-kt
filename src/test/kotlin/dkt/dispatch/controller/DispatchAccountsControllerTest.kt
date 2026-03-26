package dkt.dispatch.controller

import dkt.dispatch.dto.LoyaltyTier
import dkt.dispatch.dto.api.CreateAccountApiRequest
import dkt.dispatch.dto.service.CreateAccountServiceRequest
import dkt.dispatch.dto.service.CreateAccountServiceResponse
import dkt.dispatch.service.DispatchAccountsService
import io.mockk.every
import io.mockk.mockk
import io.mockk.slot
import io.mockk.verify
import org.junit.jupiter.api.Disabled
import org.junit.jupiter.api.Test
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import org.springframework.test.web.servlet.setup.MockMvcBuilders
import java.time.Instant
import java.util.UUID
import kotlin.test.assertEquals

class DispatchAccountsControllerTest {

    private val service = mockk<DispatchAccountsService>()
    private val mockMvc: MockMvc = MockMvcBuilders
        .standaloneSetup(DispatchAccountsController(service))
        .build()

    @Test
    fun `createAccount maps request to service and returns created account response`() {
        val capturedRequest = slot<CreateAccountServiceRequest>()
        val apiRequest = CreateAccountApiRequest(
            email = "test-user@example.com",
            fullName = "Test User",
            password = "super-secret-password",
            loyaltyTier = LoyaltyTier.Gold,
            preferredLocation = "test-location",
            note = "Test note for controller coverage.",
        )
        val responseId = UUID.fromString("11111111-1111-1111-1111-111111111111")
        val createdAt = Instant.parse("2026-03-26T17:00:00Z")
        val updatedAt = Instant.parse("2026-03-26T17:01:00Z")
        val requestJson = """
            {
              "email": "${apiRequest.email}",
              "fullName": "${apiRequest.fullName}",
              "password": "${apiRequest.password}",
              "loyaltyTier": "${apiRequest.loyaltyTier}",
              "preferredLocation": "${apiRequest.preferredLocation}",
              "note": "${apiRequest.note}"
            }
        """.trimIndent()

        every { service.createAccount(capture(capturedRequest)) } returns CreateAccountServiceResponse(
            id = responseId,
            email = apiRequest.email,
            fullName = apiRequest.fullName,
            loyaltyTier = apiRequest.loyaltyTier,
            preferredLocation = apiRequest.preferredLocation,
            note = apiRequest.note,
            createdAt = createdAt,
            updatedAt = updatedAt,
        )

        mockMvc.perform(
            post("/v1/accounts")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestJson)
        )
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.id").value(responseId.toString()))
            .andExpect(jsonPath("$.email").value(apiRequest.email))
            .andExpect(jsonPath("$.fullName").value(apiRequest.fullName))
            .andExpect(jsonPath("$.loyaltyTier").value(apiRequest.loyaltyTier!!.name))
            .andExpect(jsonPath("$.preferredLocation").value(apiRequest.preferredLocation))
            .andExpect(jsonPath("$.note").value(apiRequest.note))
            .andExpect(jsonPath("$.createdAt").value(createdAt.toString()))
            .andExpect(jsonPath("$.updatedAt").value(updatedAt.toString()))

        assertEquals(apiRequest.email, capturedRequest.captured.email)
        assertEquals(apiRequest.fullName, capturedRequest.captured.fullName)
        assertEquals(apiRequest.password, capturedRequest.captured.password)
        assertEquals(apiRequest.loyaltyTier, capturedRequest.captured.loyaltyTier)
        assertEquals(apiRequest.preferredLocation, capturedRequest.captured.preferredLocation)
        assertEquals(apiRequest.note, capturedRequest.captured.note)

        verify(exactly = 1) { service.createAccount(any()) }
    }

    @Test
    @Disabled("Verify a request with a null loyaltyTier is passed through to the service.")
    fun `createAccount allows a null loyalty tier in the request`() {
    }

    @Test
    @Disabled("Verify optional request fields can be omitted from the JSON payload.")
    fun `createAccount accepts missing optional fields`() {
    }

    @Test
    @Disabled("Verify malformed JSON receives the expected client error response.")
    fun `createAccount rejects malformed JSON`() {
    }

    @Test
    @Disabled("Verify service exceptions are translated into the expected HTTP response.")
    fun `createAccount handles service failures`() {
    }
}
