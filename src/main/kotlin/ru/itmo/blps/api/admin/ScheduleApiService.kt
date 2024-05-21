//package ru.itmo.blps.api.admin
//
//import org.jooq.exception.IntegrityConstraintViolationException
//import org.springframework.http.HttpStatus
//import org.springframework.http.ResponseEntity
//import org.springframework.stereotype.Component
//import org.springframework.transaction.annotation.Transactional
//import org.springframework.web.server.ResponseStatusException
//import ru.itmo.blps.dao.AdminScheduleDao
//import ru.itmo.blps.dao.UserScheduleDao
//import ru.itmo.blps.generated.api.admin.ScheduleDraftApiDelegate
//import ru.itmo.blps.generated.model.ScheduleDraft
//import ru.itmo.blps.generated.model.ScheduleDraftCreationRequest
//import ru.itmo.blps.generated.model.ScheduleDraftStatus
//import ru.itmo.blps.generated.model.UpdateScheduleDraftStatus
//import ru.itmo.blps.jms.JmsProducer
//import ru.itmo.blps.model.ScheduleHistory
//import ru.itmo.blps.model.ScheduleStatus
//import ru.itmo.blps.service.ScheduleDraftValidationService
//import ru.itmo.blps.service.ScheduleHistoryService
//import ru.itmo.blps.util.Mappers.toApiModel
//import ru.itmo.blps.util.Mappers.toModel
//import java.time.OffsetDateTime
//
//@Component
//class ScheduleApiService(
//        private val adminScheduleDao: AdminScheduleDao,
//        private val userScheduleDao: UserScheduleDao,
//        private val scheduleDraftValidationService: ScheduleDraftValidationService,
//        private val scheduleHistoryService: ScheduleHistoryService,
//) : ScheduleDraftApiDelegate {
//    @Transactional
//    override fun createScheduleDraft(
//            scheduleDraftCreationRequest: ScheduleDraftCreationRequest
//    ): ResponseEntity<ScheduleDraft> {
//        val draft = scheduleDraftCreationRequest.toModel()
//        val validationResult = scheduleDraftValidationService.validate(draft)
//
//        if (!validationResult.isValid) {
//            throw ResponseStatusException(HttpStatus.BAD_REQUEST, validationResult.reason)
//        }
//
//        val persistedSchedule = adminScheduleDao.insert(draft)
//
//        scheduleHistoryService.add(
//                schedule = persistedSchedule
//        )
//
//        return ResponseEntity.ok(persistedSchedule.toApiModel())
//    }
//
//    override fun getAllScheduleDrafts(): ResponseEntity<List<ScheduleDraft>> {
//        val result = adminScheduleDao.getAllByStatus(ScheduleStatus.OPENED).map { it.toApiModel() }
//        return ResponseEntity.ok(result)
//    }
//
//    @Transactional
//    override fun updateScheduleDraftStatus(
//            updateScheduleDraftStatus: UpdateScheduleDraftStatus
//    ): ResponseEntity<Void> {
//        val scheduleToUpdate = adminScheduleDao.getById(updateScheduleDraftStatus.scheduleDraftId)
//                ?: throw ResponseStatusException(HttpStatus.NOT_FOUND)
//
//        adminScheduleDao.updateStatus(
//                updateScheduleDraftStatus.scheduleDraftId,
//                ScheduleStatus.valueOf(updateScheduleDraftStatus.status.value)
//        )
//
//        val scheduleUpdated = scheduleToUpdate.copy(
//                status = ScheduleStatus.valueOf(updateScheduleDraftStatus.status.value)
//        )
//
//        if (updateScheduleDraftStatus.status == ScheduleDraftStatus.CONFIRMED) {
//            try {
//                userScheduleDao.insert(scheduleUpdated)
//            } catch (e: IntegrityConstraintViolationException) {
//                throw ResponseStatusException(HttpStatus.BAD_REQUEST, "Schedule with this date already exists")
//            }
//        }
//
//        scheduleHistoryService.add(
//                prevStatus = scheduleToUpdate.status,
//                schedule = scheduleUpdated,
//        )
//
//        return ResponseEntity(HttpStatus.OK)
//    }
//}