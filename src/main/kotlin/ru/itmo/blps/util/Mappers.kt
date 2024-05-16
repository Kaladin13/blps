package ru.itmo.blps.util

import ru.itmo.blps.generated.jooq.Tables
import ru.itmo.blps.generated.jooq.tables.records.ProgramRecord
import ru.itmo.blps.generated.jooq.tables.records.ScheduleRecord
import ru.itmo.blps.generated.model.ScheduleDraft
import ru.itmo.blps.generated.model.ScheduleDraftCreationRequest
import ru.itmo.blps.model.Program
import ru.itmo.blps.model.Schedule
import ru.itmo.blps.model.ScheduleStatus

object Mappers {

    fun ProgramRecord.toModel(): Program {
        return Program(
                id = this.id,
                name = this.name,
                startTime = this.startTime,
                endTime = this.endTime,
        )
    }

    fun ru.itmo.blps.generated.jooq.production.tables.records.ProgramRecord.toModel(): Program {
        return Program(
                id = this.id,
                name = this.name,
                startTime = this.startTime,
                endTime = this.endTime,
        )
    }

    fun Program.toApiModel(): ru.itmo.blps.generated.model.Program {
        return ru.itmo.blps.generated.model.Program()
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

    fun ru.itmo.blps.generated.model.Program.toModel(): Program {
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

    fun ScheduleRecord.toModel(programs: List<Program>): Schedule {
        return Schedule(
                id = this.get(Tables.SCHEDULE.ID),
                status = ScheduleStatus.valueOf(this.get(Tables.SCHEDULE.STATUS)),
                programs = programs,
                date = this.get(Tables.SCHEDULE.DATE),
        )
    }
}