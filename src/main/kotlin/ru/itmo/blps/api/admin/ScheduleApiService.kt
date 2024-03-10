package ru.itmo.blps.api.admin

import org.springframework.http.ResponseEntity
import ru.itmo.blps.generated.api.admin.ScheduleDraftApiDelegate
import ru.itmo.blps.generated.model.admin.ScheduleDraft
import ru.itmo.blps.generated.model.admin.ScheduleDraftCreationRequest
import ru.itmo.blps.generated.model.admin.UpdateScheduleDraftStatus

class ScheduleApiService : ScheduleDraftApiDelegate {
    override fun createScheduleDraft(
            scheduleDraftCreationRequest: ScheduleDraftCreationRequest
    ): ResponseEntity<ScheduleDraft> {
        return super.createScheduleDraft(scheduleDraftCreationRequest)
    }

    override fun getAllScheduleDrafts(): ResponseEntity<MutableList<ScheduleDraft>> {
        return super.getAllScheduleDrafts()
    }

    override fun updateScheduleDraftStatus(updateScheduleDraftStatus: MutableList<UpdateScheduleDraftStatus>?): ResponseEntity<Void> {
        return super.updateScheduleDraftStatus(updateScheduleDraftStatus)
    }
}