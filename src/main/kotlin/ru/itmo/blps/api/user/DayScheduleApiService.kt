package ru.itmo.blps.api.user

import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Component
import ru.itmo.blps.dao.ScheduleDao
import ru.itmo.blps.generated.api.user.ScheduleApi
import ru.itmo.blps.generated.model.user.Program
import java.time.OffsetDateTime

@Component
class DayScheduleApiService(
    private val scheduleDao: ScheduleDao,
) : ScheduleApi {

    override fun getScheduleForDay(datetime: OffsetDateTime): ResponseEntity<MutableList<Program>> {
        return super.getScheduleForDay(datetime)
    }
}