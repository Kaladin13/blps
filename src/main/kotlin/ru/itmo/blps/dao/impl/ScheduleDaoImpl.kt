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
import java.time.OffsetDateTime

@Repository
class ScheduleDaoImpl(
    private val dslContext: DSLContext,
) : ScheduleDao {

    @Transactional
    override fun insert(schedule: Schedule) {
        val persistedSchedule = dslContext.insertInto(SCHEDULE)
            .set(SCHEDULE.STATUS, schedule.status.toString())
            .set(SCHEDULE.DATE, schedule.date)
            .returning()
            .fetchOne() ?: throw RuntimeException("Can not persist schedule")
        schedule.programs.forEach {
            dslContext.insertInto(PROGRAM)
                .set(PROGRAM.SCHEDULE_ID, persistedSchedule.id)
                .set(PROGRAM.START_TIME, it.startTime)
                .set(PROGRAM.END_TIME, it.endTime)
                .set(PROGRAM.NAME, it.name)
                .execute()
        }
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

    @Transactional
    override fun getScheduleForDay(startTime: OffsetDateTime): List<Program> {
        val endTime = startTime.plusDays(1)

        return dslContext.selectFrom(
            SCHEDULE.join(PROGRAM)
                .on(SCHEDULE.ID.eq(PROGRAM.SCHEDULE_ID))
        ).where(SCHEDULE.STATUS.eq(ScheduleStatus.CONFIRMED.name))
            .and(PROGRAM.START_TIME.between(startTime, endTime))
            .and(PROGRAM.END_TIME.between(startTime, endTime))
            .orderBy(PROGRAM.START_TIME.asc())
            .fetch()
            .map { it.into(PROGRAM) }.map { it.toModel() }
    }

    @Transactional
    override fun updateStatus(id: Long, newStatus: ScheduleStatus) {
        if (newStatus == ScheduleStatus.CONFIRMED) {
            val alreadyConfirmed =
                dslContext.selectFrom(SCHEDULE)
                    .where(SCHEDULE.STATUS.eq(ScheduleStatus.CONFIRMED.name))
                    .fetch()

            if (alreadyConfirmed.isNotEmpty) {
                throw RuntimeException("Already has confirmed schedule")
            }
        }

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