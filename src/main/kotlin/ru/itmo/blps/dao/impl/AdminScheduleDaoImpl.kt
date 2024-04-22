package ru.itmo.blps.dao.impl

import org.jooq.DSLContext
import org.jooq.impl.DSL.selectOne
import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Transactional
import ru.itmo.blps.dao.AdminScheduleDao
import ru.itmo.blps.generated.jooq.Tables.PROGRAM
import ru.itmo.blps.generated.jooq.Tables.SCHEDULE
import ru.itmo.blps.model.Program
import ru.itmo.blps.model.Schedule
import ru.itmo.blps.model.ScheduleStatus
import ru.itmo.blps.util.Mappers.toModel
import java.time.OffsetDateTime

@Repository
class AdminScheduleDaoImpl(
        private val adminDslContext: DSLContext,
) : AdminScheduleDao {

    override fun insert(schedule: Schedule): Schedule {
        val persistedSchedule = adminDslContext.insertInto(SCHEDULE)
                .set(SCHEDULE.STATUS, schedule.status.toString())
                .set(SCHEDULE.DATE, schedule.date)
                .returning()
                .fetchOne() ?: throw RuntimeException("Can not persist schedule")
        val persistedPrograms = schedule.programs.map {
            adminDslContext.insertInto(PROGRAM)
                    .set(PROGRAM.SCHEDULE_ID, persistedSchedule.id)
                    .set(PROGRAM.START_TIME, it.startTime)
                    .set(PROGRAM.END_TIME, it.endTime)
                    .set(PROGRAM.NAME, it.name)
                    .returning()
                    .fetchOne()
                    ?.toModel() ?: throw RuntimeException("Can not persist program")
        }

        return persistedSchedule.toModel(persistedPrograms)
    }

    @Transactional(readOnly = true)
    override fun getAllByStatus(status: ScheduleStatus): List<Schedule> {
        val scheduleRecords = adminDslContext.selectFrom(SCHEDULE)
                .where(SCHEDULE.STATUS.eq(status.name))
                .fetch()
                .toList()

        if (scheduleRecords.isEmpty()) {
            return emptyList()
        }

        val programRecordsMap = adminDslContext.selectFrom(PROGRAM)
                .where(PROGRAM.SCHEDULE_ID.`in`(
                        scheduleRecords.map { it.id }
                )
                )
                .fetchGroups(PROGRAM.SCHEDULE_ID)

        val result = ArrayList<Schedule>()
        for (scheduleRecord in scheduleRecords) {
            val schedule = scheduleRecord.toModel(
                    programRecordsMap[scheduleRecord.id]?.map { it.toModel() }
                            ?: emptyList()
            )

            result.add(schedule)
        }

        return result
    }

    @Transactional
    @Deprecated("Use UserScheduleDaoImpl::getScheduleForDay")
    override fun getScheduleForDay(startTime: OffsetDateTime): List<Program> {
        val endTime = startTime.plusDays(1)

        return adminDslContext.selectFrom(
                SCHEDULE.join(PROGRAM)
                        .on(SCHEDULE.ID.eq(PROGRAM.SCHEDULE_ID))
        ).where(SCHEDULE.STATUS.eq(ScheduleStatus.CONFIRMED.name))
                .and(PROGRAM.START_TIME.between(startTime, endTime))
                .and(PROGRAM.END_TIME.between(startTime, endTime))
                .orderBy(PROGRAM.START_TIME.asc())
                .fetch()
                .map { it.into(PROGRAM) }.map { it.toModel() }
    }

    override fun getById(id: Long): Schedule? {
        val programs = adminDslContext.selectFrom(PROGRAM)
                .where(PROGRAM.SCHEDULE_ID.eq(id))
                .toList()
                .map { it.toModel() }

        return adminDslContext.selectFrom(SCHEDULE)
                .where(SCHEDULE.ID.eq(id))
                .fetchOne()
                ?.toModel(programs)
    }

    @Transactional
    override fun updateStatus(id: Long, newStatus: ScheduleStatus) {
        if (newStatus == ScheduleStatus.CONFIRMED) {
            val alreadyConfirmed =
                    adminDslContext.selectFrom(SCHEDULE)
                            .where(SCHEDULE.STATUS.eq(ScheduleStatus.CONFIRMED.name))
                            .and(SCHEDULE.ID.eq(id))
                            .fetch()

            if (alreadyConfirmed.isNotEmpty) {
                throw RuntimeException("Already confirmed schedule")
            }
        }

        adminDslContext.update(SCHEDULE)
                .set(SCHEDULE.STATUS, newStatus.name)
                .where(SCHEDULE.ID.eq(id))
                .execute()
    }

    @Transactional(readOnly = true)
    override fun exists(id: Long): Boolean {
        return adminDslContext.fetchExists(
                selectOne().from(SCHEDULE)
                        .where(SCHEDULE.ID.eq(id))
        )
    }
}