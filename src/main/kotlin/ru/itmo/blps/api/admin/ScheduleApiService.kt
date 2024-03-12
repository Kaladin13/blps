package ru.itmo.blps.api.admin

import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Component
import org.springframework.web.server.ResponseStatusException
import ru.itmo.blps.dao.ScheduleDao
import ru.itmo.blps.generated.api.admin.ScheduleDraftApiDelegate
import ru.itmo.blps.generated.model.admin.ScheduleDraft
import ru.itmo.blps.generated.model.admin.ScheduleDraftCreationRequest
import ru.itmo.blps.generated.model.admin.UpdateScheduleDraftStatus
import ru.itmo.blps.model.ScheduleStatus
import ru.itmo.blps.util.Mappers.toApiModel
import ru.itmo.blps.util.Mappers.toModel

@Component
class ScheduleApiService(
    private val scheduleDao: ScheduleDao,
) : ScheduleDraftApiDelegate {
    override fun createScheduleDraft(
        scheduleDraftCreationRequest: ScheduleDraftCreationRequest
    ): ResponseEntity<ScheduleDraft> {
        scheduleDao.insert(scheduleDraftCreationRequest.toModel())

        return ResponseEntity(HttpStatus.OK)
    }

    override fun getAllScheduleDrafts(): ResponseEntity<List<ScheduleDraft>> {
        val result = scheduleDao.getAllByStatus(ScheduleStatus.OPENED).map { it.toApiModel() }
        return ResponseEntity.ok(result)
    }

    override fun updateScheduleDraftStatus(
        updateScheduleDraftStatus: UpdateScheduleDraftStatus
    ): ResponseEntity<Void> {
        val exists = scheduleDao.exists(updateScheduleDraftStatus.scheduleDraftId)
        if (!exists) {
            throw ResponseStatusException(HttpStatus.NOT_FOUND)
        }
        scheduleDao.updateStatus(
            updateScheduleDraftStatus.scheduleDraftId,
            ScheduleStatus.valueOf(updateScheduleDraftStatus.status.value)
        )

        return ResponseEntity(HttpStatus.OK)
    }
}