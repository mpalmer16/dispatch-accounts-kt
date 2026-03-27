package dkt.dispatch.controller

import dkt.dispatch.dto.api.CreateAccountApiRequest
import dkt.dispatch.dto.api.CreateAccountApiResponse
import dkt.dispatch.dto.api.GerAccountApiResponse
import dkt.dispatch.repository.toAccountResponse
import dkt.dispatch.service.DispatchAccountsService
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/v1/accounts")
class DispatchAccountsController(
    private val service: DispatchAccountsService
) {

    @PostMapping
    fun createAccount(
        @RequestBody(required = true) request: CreateAccountApiRequest
    ): CreateAccountApiResponse {
        val serviceRequest = request.toServiceRequest()
        val response = service.createAccount(serviceRequest)
        return response!!.toApiResponse()
    }

    @GetMapping("/{accountId}")
    fun getAccountById(
        @PathVariable("accountId") accountId: String
    ): GerAccountApiResponse? {
        val response = service.getAccountById(accountId)
        return response?.toAccountResponse()
    }
}