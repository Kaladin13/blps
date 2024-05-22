package ru.itmo.blps.delegates

import org.camunda.bpm.engine.delegate.DelegateExecution
import org.camunda.bpm.engine.delegate.JavaDelegate
import org.springframework.stereotype.Component

@Component
class SubmitValidation : JavaDelegate {
    override fun execute(p0: DelegateExecution?) {
        println("I AM GOOD")
    }
}