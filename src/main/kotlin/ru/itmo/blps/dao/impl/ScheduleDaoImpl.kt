package ru.itmo.blps.dao.impl

import org.jooq.DSLContext
import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Transactional
import ru.itmo.blps.dao.ScheduleDao
import ru.itmo.blps.generated.jooq.Tables.PROGRAM
import ru.itmo.blps.generated.jooq.Tables.SCHEDULE
import ru.itmo.blps.generated.jooq.tables.records.ProgramRecord
import ru.itmo.blps.generated.jooq.tables.records.ScheduleRecord
import ru.itmo.blps.model.Program
import ru.itmo.blps.model.Schedule
import ru.itmo.blps.model.ScheduleStatus

@Repository
class ScheduleDaoImpl(
        private val dslContext: DSLContext,
) : ScheduleDao {

    @Transactional
    override fun insert(schedule: Schedule) {
        dslContext.batchInsert(
                schedule.programs.map { it.toRecord(schedule.id) }
        )

        dslContext.batchInsert(
                schedule.toRecord()
        )
    }

    @Transactional(readOnly = true)
    override fun getAllByStatus(status: ScheduleStatus): List<Schedule> {
        val scheduleRecords = dslContext.selectFrom(SCHEDULE)
                .where(SCHEDULE.STATUS.eq(status.name))
                .fetch()
                .toList()

        val programRecordsMap = dslContext.selectFrom(PROGRAM)
                .where(PROGRAM.SCHEDULE_ID.`in`(
                        scheduleRecords.map { it.id }
                )
                )
                .fetchGroups(PROGRAM.SCHEDULE_ID)

        val result = ArrayList<Schedule>()
        for (scheduleRecord in scheduleRecords) {
            val schedule = Schedule(
                    id = scheduleRecord.id,
                    status = ScheduleStatus.valueOf(scheduleRecord.status),
                    date = scheduleRecord.date,
                    programs = programRecordsMap[scheduleRecord.id]?.map { it.toModel() }
                            ?: emptyList()
            )
            result.add(schedule)
        }

        return result
    }

    @Transactional
    override fun updateStatus(id: Long, newStatus: ScheduleStatus) {
        dslContext.update(SCHEDULE)
                .set(SCHEDULE.STATUS, newStatus.name)
                .where(SCHEDULE.ID.eq(id))
                .execute()
    }

    private fun Schedule.toRecord(): ScheduleRecord {
        return ScheduleRecord(
                this.id, this.status.toString(), this.date
        )
    }

    private fun Program.toRecord(scheduleId: Long): ProgramRecord {
        return ProgramRecord(
                this.id, scheduleId, this.startTime, this.endTime, this.name
        )
    }

    private fun ProgramRecord.toModel(): Program {
        return Program(
                id = this.id,
                name = this.name,
                startTime = this.startTime,
                endTime = this.endTime,
        )
    }
}