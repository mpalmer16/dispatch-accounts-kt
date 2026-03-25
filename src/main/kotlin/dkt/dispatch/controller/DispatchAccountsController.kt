package dkt.dispatch.controller

import dkt.dispatch.dto.api.CreateAccountApiRequest
import dkt.dispatch.dto.api.CreateAccountApiResponse
import dkt.dispatch.service.DispatchAccountsService
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/v1/accounts")
class DispatchAccountsController(
    private val service: DispatchAccountsService
) {

    @PostMapping
    fun createAccount(
        @RequestBody(required = true) request: CreateAccountApiRequest
    ): CreateAccountApiResponse {
        val response = service.createAccount(request.toServiceRequest())
        return response!!.toApiResponse()
    }
}