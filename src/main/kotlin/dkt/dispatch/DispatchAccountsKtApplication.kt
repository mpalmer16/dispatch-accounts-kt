package dkt.dispatch

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.properties.ConfigurationPropertiesScan
import org.springframework.boot.runApplication

@ConfigurationPropertiesScan
@SpringBootApplication
class DispatchAccountsKtApplication

fun main(args: Array<String>) {
    runApplication<DispatchAccountsKtApplication>(*args)
}