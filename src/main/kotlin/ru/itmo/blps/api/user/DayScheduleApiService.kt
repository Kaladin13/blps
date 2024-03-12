package ru.itmo.blps.api.user

import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Component
import ru.itmo.blps.dao.ScheduleDao
import ru.itmo.blps.generated.api.user.ScheduleApi
import ru.itmo.blps.generated.model.Program
import ru.itmo.blps.util.Mappers.toApiModel
import java.time.OffsetDateTime

@Component
class DayScheduleApiService(
    private val scheduleDao: ScheduleDao,
) : ScheduleApi {

    override fun getScheduleForDay(datetime: OffsetDateTime): ResponseEntity<List<Program>> {
        val scheduleForDay = scheduleDao.getScheduleForDay(datetime).map { it.toApiModel() }
        return ResponseEntity.ok(scheduleForDay)
    }
}