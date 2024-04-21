package ru.itmo.blps.model

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonProperty
import java.time.OffsetDateTime

data class Program
@JsonCreator
constructor(
        @JsonProperty("id")
        val id: Long? = null,
        @JsonProperty("name")
        val name: String,
        @JsonProperty("startTime")
        val startTime: OffsetDateTime,
        @JsonProperty("endTime")
        val endTime: OffsetDateTime,
)
