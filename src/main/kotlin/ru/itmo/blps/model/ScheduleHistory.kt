package ru.itmo.blps.model

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonProperty
import java.time.OffsetDateTime

data class ScheduleHistory
@JsonCreator
constructor(
        @JsonProperty("id")
        val id: Long? = null,
        @JsonProperty("scheduleDraftId")
        val scheduleDraftId: Long,
        @JsonProperty("prevStatus")
        val prevStatus: ScheduleStatus?,
        @JsonProperty("currentStatus")
        val currentStatus: ScheduleStatus,
        @JsonProperty("programs")
        val programs: List<Program>,
        @JsonProperty("date")
        val date: OffsetDateTime,
        @JsonProperty("updatedAt")
        val updatedAt: OffsetDateTime,
)