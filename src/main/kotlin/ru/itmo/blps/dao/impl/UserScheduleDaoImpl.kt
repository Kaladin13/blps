//package ru.itmo.blps.dao.impl
//
//import org.jooq.DSLContext
//import org.springframework.stereotype.Repository
//import org.springframework.transaction.annotation.Transactional
//import ru.itmo.blps.dao.UserScheduleDao
//import ru.itmo.blps.generated.jooq.production.Tables.PROGRAM
//import ru.itmo.blps.generated.jooq.production.Tables.SCHEDULE
//import ru.itmo.blps.model.Program
//import ru.itmo.blps.model.Schedule
//import ru.itmo.blps.util.Mappers.toModel
//import java.time.OffsetDateTime
//
//@Repository
//class UserScheduleDaoImpl(
//    private val userDslContext: DSLContext
//): UserScheduleDao {
//    override fun insert(schedule: Schedule) {
//        val persistedSchedule = userDslContext.insertInto(SCHEDULE)
//            .set(SCHEDULE.DATE, schedule.date)
//            .returning()
//            .fetchOne() ?: throw RuntimeException("Can not persist schedule")
//
//        schedule.programs.forEach {
//            userDslContext.insertInto(PROGRAM)
//                .set(PROGRAM.SCHEDULE_ID, persistedSchedule.id)
//                .set(PROGRAM.START_TIME, it.startTime)
//                .set(PROGRAM.END_TIME, it.endTime)
//                .set(PROGRAM.NAME, it.name)
//                .execute()
//        }
//    }
//
//    @Transactional(transactionManager = "transactionManager")
//    override fun getScheduleForDay(startTime: OffsetDateTime): List<Program> {
//        val endTime = startTime.plusDays(1)
//
//        return userDslContext.selectFrom(
//            SCHEDULE.join(PROGRAM)
//                .on(SCHEDULE.ID.eq(PROGRAM.SCHEDULE_ID))
//        ).where(PROGRAM.START_TIME.between(startTime, endTime))
//            .and(PROGRAM.END_TIME.between(startTime, endTime))
//            .orderBy(PROGRAM.START_TIME.asc())
//            .fetch()
//            .map { it.into(PROGRAM) }.map { it.toModel() }
//    }
//}