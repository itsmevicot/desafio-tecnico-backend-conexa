package com.conexa.backend.scheduling.presentation.api.v1.controllers;

import com.conexa.backend.scheduling.application.services.ScheduleService;
import com.conexa.backend.scheduling.domain.models.Schedule;
import com.conexa.backend.scheduling.presentation.api.v1.BaseV1Controller;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Schedule")
@RestController
@RequestMapping("/schedule")
@RequiredArgsConstructor
public class ScheduleController extends BaseV1Controller {

    private final ScheduleService scheduleService;

    /**
     * Creates a new schedule.
     *
     * @param schedule the schedule information
     * @return the created schedule
     */
    @PostMapping
    public ResponseEntity<Schedule> createSchedule(@RequestBody Schedule schedule) {
        Schedule createdSchedule = scheduleService.createSchedule(schedule);
        return ResponseEntity.ok(createdSchedule);
    }
}
