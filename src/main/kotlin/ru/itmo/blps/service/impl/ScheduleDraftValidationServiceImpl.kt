package ru.itmo.blps.service.impl

import org.springframework.stereotype.Service
import ru.itmo.blps.model.Program
import ru.itmo.blps.model.Schedule
import ru.itmo.blps.model.ScheduleDraftValidationResult
import ru.itmo.blps.service.ScheduleDraftValidationService

@Service
class ScheduleDraftValidationServiceImpl : ScheduleDraftValidationService {

    override fun validate(scheduleDraft: Schedule): ScheduleDraftValidationResult {
        if (scheduleDraft.programs.any { it.startTime.toLocalDate() != scheduleDraft.date.toLocalDate() }) {
            return ScheduleDraftValidationResult(
                    isValid = false,
                    reason = "All programs start date must be equal to schedule start date"
            )
        }

        if (scheduleDraft.programs.any { it.startTime.isAfter(it.endTime) ||  it.startTime.isEqual(it.endTime) }) {
            return ScheduleDraftValidationResult(
                    isValid = false,
                    reason = "Programs start time must be less than end time"
            )
        }

        scheduleDraft.programs.forEach { program ->
            val others = scheduleDraft.programs - program
            others.forEach { other ->
                if (doProgramsOverlap(program, other)) {
                    return ScheduleDraftValidationResult(
                            isValid = false,
                            reason = "Programs start and end time must not intersect"
                    )
                }
            }
        }

        return ScheduleDraftValidationResult(true)
    }

    private fun doProgramsOverlap(program1: Program, program2: Program): Boolean {
        // Первая программа заканчивается до начала второй или вторая программа заканчивается до начала первой
        return !(program1.endTime <= program2.startTime || program2.endTime <= program1.startTime)
    }

}