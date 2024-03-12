package ru.itmo.blps.service

import ru.itmo.blps.model.Schedule
import ru.itmo.blps.model.ScheduleDraftValidationResult

interface ScheduleDraftValidationService {

    fun validate(scheduleDraft: Schedule): ScheduleDraftValidationResult
}