package ru.itmo.blps.util

import ru.itmo.blps.generated.jooq.tables.records.ProgramRecord
import ru.itmo.blps.generated.jooq.tables.records.ScheduleRecord
import ru.itmo.blps.generated.model.admin.ScheduleDraft
import ru.itmo.blps.generated.model.admin.ScheduleDraftCreationRequest
import ru.itmo.blps.model.Program
import ru.itmo.blps.model.Schedule
import ru.itmo.blps.model.ScheduleStatus
import ru.itmo.blps.util.Mappers.toModel

object Mappers {

    fun Schedule.toRecord(): ScheduleRecord {
        return ScheduleRecord(
            this.id, this.status.toString(), this.date
        )
    }

    fun Program.toRecord(scheduleId: Long): ProgramRecord {
        return ProgramRecord(
            this.id, scheduleId, this.startTime, this.endTime, this.name
        )
    }

    fun ProgramRecord.toModel(): Program {
        return Program(
            id = this.id,
            name = this.name,
            startTime = this.startTime,
            endTime = this.endTime,
        )
    }

    fun Program.toApiModel(): ru.itmo.blps.generated.model.admin.Program {
        return ru.itmo.blps.generated.model.admin.Program()
            .name(this.name)
            .startTime(this.startTime)
            .endTime(this.endTime)
    }

    fun Schedule.toApiModel(): ScheduleDraft {
        return ScheduleDraft()
            .id(this.id)
            .dateOfSchedule(this.date)
            .programs(this.programs.map { it.toApiModel() })
    }

    fun ru.itmo.blps.generated.model.admin.Program.toModel(): Program {
        return Program(
            name = this.name,
            startTime = this.startTime,
            endTime = this.endTime
        )
    }

    fun ScheduleDraftCreationRequest.toModel(): Schedule {
        return Schedule(
            status = ScheduleStatus.OPENED,
            programs = this.programs.map { it.toModel() },
            date = this.dateOfSchedule
        )
    }
}