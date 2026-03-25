package dkt.dispatch.repository

import org.springframework.data.repository.CrudRepository
import java.util.*

interface DispatchAccountsRepository : CrudRepository<AccountEntity, UUID> {
    fun findByEmail(email: String): AccountEntity?
}