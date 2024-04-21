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
import ru.itmo.blps.model.ScheduleStatus
import ru.itmo.blps.service.ScheduleDraftValidationService
import ru.itmo.blps.util.Mappers.toApiModel
import ru.itmo.blps.util.Mappers.toModel

@Component
class ScheduleApiService(
    private val adminScheduleDao: AdminScheduleDao,
    private val userScheduleDao: UserScheduleDao,
    private val scheduleDraftValidationService: ScheduleDraftValidationService,
) : ScheduleDraftApiDelegate {
    override fun createScheduleDraft(
            scheduleDraftCreationRequest: ScheduleDraftCreationRequest
    ): ResponseEntity<ScheduleDraft> {
        val draft = scheduleDraftCreationRequest.toModel()
        val validationResult = scheduleDraftValidationService.validate(draft)

        if (!validationResult.isValid) {
            throw ResponseStatusException(HttpStatus.BAD_REQUEST, validationResult.reason)
        }
        adminScheduleDao.insert(draft)
        return ResponseEntity.ok(draft.toApiModel())
    }

    override fun getAllScheduleDrafts(): ResponseEntity<List<ScheduleDraft>> {
        val result = adminScheduleDao.getAllByStatus(ScheduleStatus.OPENED).map { it.toApiModel() }
        return ResponseEntity.ok(result)
    }

    @Transactional(transactionManager = "transactionManager")
    override fun updateScheduleDraftStatus(
            updateScheduleDraftStatus: UpdateScheduleDraftStatus
    ): ResponseEntity<Void> {
        val exists = adminScheduleDao.exists(updateScheduleDraftStatus.scheduleDraftId)
        if (!exists) {
            throw ResponseStatusException(HttpStatus.NOT_FOUND)
        }
        adminScheduleDao.updateStatus(
                updateScheduleDraftStatus.scheduleDraftId,
                ScheduleStatus.valueOf(updateScheduleDraftStatus.status.value)
        )

        if (updateScheduleDraftStatus.status == ScheduleDraftStatus.CONFIRMED) {
            val schedule = adminScheduleDao.getById(updateScheduleDraftStatus.scheduleDraftId)
                    ?: error("Schedule draft not found in admin db after update")
            try {
                userScheduleDao.insert(schedule)
            } catch (e: IntegrityConstraintViolationException) {
                throw ResponseStatusException(HttpStatus.BAD_REQUEST, "Schedule with this date already exists")
            }
        }

        return ResponseEntity(HttpStatus.OK)
    }
}