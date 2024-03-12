package ru.itmo.blps.model

data class ScheduleDraftValidationResult(
        val isValid: Boolean,
        val reason: String? = null,
)
