package ru.itmo.blps.api.admin

import org.jooq.exception.IntegrityConstraintViolationException
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.server.ResponseStatusException
import ru.itmo.blps.dao.AdminScheduleDao
import ru.itmo.blps.dao.UserScheduleDao
import ru.itmo.blps.generated.api.admin.ScheduleDraftApiDelegate
import ru.itmo.blps.generated.model.ScheduleDraft
import ru.itmo.blps.generated.model.ScheduleDraftCreationRequest
import ru.itmo.blps.generated.model.ScheduleDraftStatus
import ru.itmo.blps.generated.model.UpdateScheduleDraftStatus
import ru.itmo.blps.jms.JmsService
import ru.itmo.blps.model.ScheduleHistory
import ru.itmo.blps.model.ScheduleStatus
import ru.itmo.blps.service.ScheduleDraftValidationService
import ru.itmo.blps.util.Mappers.toApiModel
import ru.itmo.blps.util.Mappers.toModel
import java.time.OffsetDateTime

@Component
class ScheduleApiService(
        private val adminScheduleDao: AdminScheduleDao,
        private val userScheduleDao: UserScheduleDao,
        private val scheduleDraftValidationService: ScheduleDraftValidationService,
        private val jmsService: JmsService
) : ScheduleDraftApiDelegate {
    @Transactional
    override fun createScheduleDraft(
            scheduleDraftCreationRequest: ScheduleDraftCreationRequest
    ): ResponseEntity<ScheduleDraft> {
        val draft = scheduleDraftCreationRequest.toModel()
        val validationResult = scheduleDraftValidationService.validate(draft)

        if (!validationResult.isValid) {
            throw ResponseStatusException(HttpStatus.BAD_REQUEST, validationResult.reason)
        }

        val persistedId = adminScheduleDao.insert(draft)

        jmsService.sendMessage(
                JMS_QUEUE,
                ScheduleHistory(
                        id = persistedId,
                        prevStatus = null,
                        currentStatus = draft.status,
                        date = draft.date,
                        changeTime = OffsetDateTime.now(),
                        programs = draft.programs,
                )
        )
        return ResponseEntity.ok(draft.copy(id = persistedId).toApiModel())
    }

    override fun getAllScheduleDrafts(): ResponseEntity<List<ScheduleDraft>> {
        val result = adminScheduleDao.getAllByStatus(ScheduleStatus.OPENED).map { it.toApiModel() }
        return ResponseEntity.ok(result)
    }

    @Transactional(transactionManager = "transactionManager")
    override fun updateScheduleDraftStatus(
            updateScheduleDraftStatus: UpdateScheduleDraftStatus
    ): ResponseEntity<Void> {
        val scheduleToUpdate = adminScheduleDao.getById(updateScheduleDraftStatus.scheduleDraftId)
                ?: throw ResponseStatusException(HttpStatus.NOT_FOUND)

        adminScheduleDao.updateStatus(
                updateScheduleDraftStatus.scheduleDraftId,
                ScheduleStatus.valueOf(updateScheduleDraftStatus.status.value)
        )

        val scheduleUpdated = scheduleToUpdate.copy(
                status = ScheduleStatus.valueOf(updateScheduleDraftStatus.status.value)
        )

        if (updateScheduleDraftStatus.status == ScheduleDraftStatus.CONFIRMED) {
            try {
                userScheduleDao.insert(scheduleUpdated)
            } catch (e: IntegrityConstraintViolationException) {
                throw ResponseStatusException(HttpStatus.BAD_REQUEST, "Schedule with this date already exists")
            }
        }

        jmsService.sendMessage(
                JMS_QUEUE,
                ScheduleHistory(
                        id = scheduleToUpdate.id!!,
                        prevStatus = scheduleToUpdate.status,
                        currentStatus = scheduleUpdated.status,
                        date = scheduleUpdated.date,
                        changeTime = OffsetDateTime.now(),
                        programs = scheduleUpdated.programs,
                )
        )

        return ResponseEntity(HttpStatus.OK)
    }

    companion object {
        private const val JMS_QUEUE = "s"
    }
}