//package ru.itmo.blps.dao.impl
//
//import org.jooq.DSLContext
//import org.springframework.stereotype.Repository
//import ru.itmo.blps.dao.ScheduleHistoryDao
//import ru.itmo.blps.generated.jooq.Tables.SCHEDULE_HISTORY
//import ru.itmo.blps.model.ScheduleHistory
//
//@Repository
//class ScheduleHistoryDaoImpl(
//    private val adminDslContext: DSLContext
//) : ScheduleHistoryDao {
//    override fun insert(scheduleHistory: ScheduleHistory): Long {
//        return adminDslContext.insertInto(SCHEDULE_HISTORY)
//                .set(SCHEDULE_HISTORY.SCHEDULE_DRAFT_ID, scheduleHistory.scheduleDraftId)
//                .set(SCHEDULE_HISTORY.PREVIOUS_STATUS, scheduleHistory.prevStatus?.name)
//                .set(SCHEDULE_HISTORY.CURRENT_STATUS, scheduleHistory.currentStatus.name)
//                .set(SCHEDULE_HISTORY.UPDATED_AT, scheduleHistory.updatedAt)
//                .set(SCHEDULE_HISTORY.DATE, scheduleHistory.date)
//                .returning()
//                .fetchOne()
//                ?.id ?: throw RuntimeException("Can not persist schedule history")
//    }
//}