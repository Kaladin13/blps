package ru.itmo.blps.dao.impl

import org.jooq.DSLContext
import org.jooq.impl.DSL.selectOne
import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Transactional
import ru.itmo.blps.dao.ScheduleDao
import ru.itmo.blps.generated.jooq.Tables.PROGRAM
import ru.itmo.blps.generated.jooq.Tables.SCHEDULE
import ru.itmo.blps.model.Program
import ru.itmo.blps.model.Schedule
import ru.itmo.blps.model.ScheduleStatus
import ru.itmo.blps.util.Mappers.toModel
import ru.itmo.blps.util.Mappers.toRecord
import java.time.OffsetDateTime

@Repository
class ScheduleDaoImpl(
        private val dslContext: DSLContext,
) : ScheduleDao {

    @Transactional
    override fun insert(schedule: Schedule) {
        val persistedSchedule = dslContext.insertInto(SCHEDULE)
                .set(schedule.toRecord())
                .returning()
                .fetchOne() ?: throw RuntimeException("Can not persist schedule")

        dslContext.batchInsert(
                schedule.programs.map { it.toRecord(persistedSchedule.id)  }
        )
    }

    @Transactional(readOnly = true)
    override fun getAllByStatus(status: ScheduleStatus): List<Schedule> {
        val scheduleRecords = dslContext.selectFrom(SCHEDULE)
                .where(SCHEDULE.STATUS.eq(status.name))
                .fetch()
                .toList()

        if (scheduleRecords.isEmpty()) {
            return emptyList()
        }

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

    override fun getScheduleForDay(startTime: OffsetDateTime): List<Program> {
        TODO("Not yet implemented")
    }

    @Transactional
    override fun updateStatus(id: Long, newStatus: ScheduleStatus) {
        dslContext.update(SCHEDULE)
                .set(SCHEDULE.STATUS, newStatus.name)
                .where(SCHEDULE.ID.eq(id))
                .execute()
    }

    @Transactional(readOnly = true)
    override fun exists(id: Long): Boolean {
        return dslContext.fetchExists(
                selectOne().from(SCHEDULE)
                        .where(SCHEDULE.ID.eq(id))
        )
    }
}