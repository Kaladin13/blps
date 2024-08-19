package ru.itmo.blps.api.user

import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Component
import ru.itmo.blps.dao.AdminScheduleDao
import ru.itmo.blps.dao.UserScheduleDao
import ru.itmo.blps.generated.api.user.ScheduleApiDelegate
import ru.itmo.blps.generated.model.Program
import ru.itmo.blps.util.Mappers.toApiModel
import java.time.OffsetDateTime

@Component
class DayScheduleApiService(
    private val userScheduleDao: UserScheduleDao,
) : ScheduleApiDelegate {

    override fun getScheduleForDay(datetime: OffsetDateTime): ResponseEntity<List<Program>> {
        val scheduleForDay = userScheduleDao.getScheduleForDay(datetime).map { it.toApiModel() }

        return ResponseEntity.ok(scheduleForDay)
    }
}