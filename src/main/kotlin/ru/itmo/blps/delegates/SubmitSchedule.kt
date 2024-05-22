package ru.itmo.blps.delegates

import lombok.extern.slf4j.Slf4j
import org.camunda.bpm.engine.delegate.DelegateExecution
import org.camunda.bpm.engine.delegate.JavaDelegate
import org.springframework.stereotype.Component

@Component
@Slf4j
class SubmitSchedule : JavaDelegate {
    val firstProperty = "First property"
    override fun execute(p0: DelegateExecution?) {
        val rr = p0?.getVariable("program_name_var")
        println(rr)

        p0?.setVariable("reject", "false")
    }
}